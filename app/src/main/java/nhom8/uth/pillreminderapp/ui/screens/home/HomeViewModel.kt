package nhom8.uth.pillreminderapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import nhom8.uth.pillreminderapp.data.repository.MedicineRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * Data class đại diện cho một reminder với status
 */
data class MedicineReminder(
    val medicine: MedicineEntity,
    val reminderTime: String,
    val status: ReminderStatus,
    val historyId: Long? = null // ID của history entry nếu đã có
)

/**
 * Trạng thái của reminder
 */
enum class ReminderStatus {
    PENDING,    // Chưa đến giờ
    COMPLETED,  // Đã uống
    BEFORE_EATING, // Trước bữa ăn (pending với intake advice)
    SKIPPED,    // Đã bỏ qua
    MISSED      // Đã quá giờ nhưng chưa uống
}

/**
 * ViewModel quản lý logic cho màn hình Home
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {
    
    private val currentDate = Date()
    
    // StateFlow cho overdue medicines
    private val _overdueMedicines = MutableStateFlow<List<MedicineReminder>>(emptyList())
    val overdueMedicines: StateFlow<List<MedicineReminder>> = _overdueMedicines.asStateFlow()
    
    // StateFlow cho today's schedule
    private val _todaySchedule = MutableStateFlow<List<MedicineReminder>>(emptyList())
    val todaySchedule: StateFlow<List<MedicineReminder>> = _todaySchedule.asStateFlow()
    
    // StateFlow cho completed medicines
    private val _completedMedicines = MutableStateFlow<List<MedicineReminder>>(emptyList())
    val completedMedicines: StateFlow<List<MedicineReminder>> = _completedMedicines.asStateFlow()
    
    // StateFlow cho loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    init {
        loadMedicines()
    }
    
    /**
     * Load medicines và phân loại thành overdue và today's schedule
     */
    fun loadMedicines() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Lấy tất cả active medicines
                val activeMedicines = repository.getActiveMedicines()
                    .first()
                
                // Lấy today's history
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                val startOfDay = calendar.time
                
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                calendar.set(Calendar.MINUTE, 59)
                calendar.set(Calendar.SECOND, 59)
                val endOfDay = calendar.time
                
                val todayHistory = repository.getTodayHistory(startOfDay, endOfDay)
                
                // Tạo map để tra cứu nhanh history theo medicineId và time
                val historyMap = todayHistory.associateBy { 
                    "${it.medicineId}_${it.takenTime}" 
                }
                
                // Lấy overdue medicines (chỉ MISSED, không bao gồm COMPLETED)
                val overdue = repository.getOverdueMedicines(currentDate)
                val overdueReminders = overdue.flatMap { medicine ->
                    medicine.reminderTimes.map { time ->
                        val historyKey = "${medicine.id}_$time"
                        val history = historyMap[historyKey]
                        val status = when {
                            history != null -> when (history.status) {
                                HistoryEntity.STATUS_TAKEN -> ReminderStatus.COMPLETED
                                HistoryEntity.STATUS_SKIPPED -> ReminderStatus.SKIPPED
                                else -> ReminderStatus.MISSED
                            }
                            isTimePassed(time) -> ReminderStatus.MISSED
                            medicine.intakeAdvice != "None" -> ReminderStatus.BEFORE_EATING
                            else -> ReminderStatus.PENDING
                        }
                        MedicineReminder(
                            medicine = medicine,
                            reminderTime = time,
                            status = status,
                            historyId = history?.id
                        )
                    }
                }.filter { it.status == ReminderStatus.MISSED }
                
                // Lấy today's schedule (medicines có reminder times hôm nay)
                val todayMedicines = repository.getMedicinesByDate(currentDate)
                val allTodayReminders = todayMedicines.flatMap { medicine ->
                    medicine.reminderTimes.map { time ->
                        val historyKey = "${medicine.id}_$time"
                        val history = historyMap[historyKey]
                        val status = when {
                            history != null -> when (history.status) {
                                HistoryEntity.STATUS_TAKEN -> ReminderStatus.COMPLETED
                                HistoryEntity.STATUS_SKIPPED -> ReminderStatus.SKIPPED
                                else -> ReminderStatus.MISSED
                            }
                            isTimePassed(time) -> ReminderStatus.MISSED
                            medicine.intakeAdvice != "None" -> ReminderStatus.BEFORE_EATING
                            else -> ReminderStatus.PENDING
                        }
                        MedicineReminder(
                            medicine = medicine,
                            reminderTime = time,
                            status = status,
                            historyId = history?.id
                        )
                    }
                }
                
                // Tách completed reminders
                val completedReminders = (overdueReminders + allTodayReminders)
                    .filter { it.status == ReminderStatus.COMPLETED }
                    .distinctBy { "${it.medicine.id}_${it.reminderTime}" }
                    .sortedBy { it.reminderTime }
                
                // Today's schedule: loại bỏ completed và overdue
                val todayReminders = allTodayReminders.filter { 
                    it.status != ReminderStatus.COMPLETED &&
                    !overdueReminders.any { overdue -> 
                        overdue.medicine.id == it.medicine.id && 
                        overdue.reminderTime == it.reminderTime 
                    }
                }
                
                _overdueMedicines.value = overdueReminders
                _todaySchedule.value = todayReminders
                _completedMedicines.value = completedReminders
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Kiểm tra xem thời gian đã qua chưa
     */
    private fun isTimePassed(timeString: String): Boolean {
        return try {
            val calendar = Calendar.getInstance()
            val timeParts = timeString.split(":")
            val hour = timeParts[0].toInt()
            val minute = timeParts[1].split(" ")[0].toInt()
            val isPM = timeString.contains("PM", ignoreCase = true)
            
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)
            
            val reminderHour = if (isPM && hour != 12) hour + 12 else if (!isPM && hour == 12) 0 else hour
            val reminderTimeInMinutes = reminderHour * 60 + minute
            val currentTimeInMinutes = currentHour * 60 + currentMinute
            
            currentTimeInMinutes > reminderTimeInMinutes
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Đánh dấu thuốc đã uống
     */
    fun markAsTaken(reminder: MedicineReminder) {
        viewModelScope.launch {
            try {
                repository.markMedicineAsTaken(
                    medicineId = reminder.medicine.id,
                    date = currentDate,
                    time = reminder.reminderTime
                )
                loadMedicines() // Reload để cập nhật UI
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Đánh dấu thuốc đã bỏ qua
     */
    fun markAsSkipped(reminder: MedicineReminder) {
        viewModelScope.launch {
            try {
                repository.markMedicineAsSkipped(
                    medicineId = reminder.medicine.id,
                    date = currentDate,
                    time = reminder.reminderTime
                )
                loadMedicines() // Reload để cập nhật UI
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    /**
     * Xóa thuốc
     */
    fun deleteMedicine(medicineId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteMedicineById(medicineId)
                loadMedicines() // Reload để cập nhật UI
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
