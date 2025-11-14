package nhom8.uth.pillreminderapp.util

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import nhom8.uth.pillreminderapp.workers.ReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Utility class để lên lịch nhắc nhở với WorkManager
 */
@Singleton
class AlarmScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val workManager: WorkManager
) {
    
    /**
     * Lên lịch nhắc nhở cho một thuốc
     */
    fun scheduleReminder(medicine: MedicineEntity) {
        // Cancel existing reminders for this medicine
        cancelReminder(medicine.id)
        
        val currentDate = Calendar.getInstance()
        val startDate = Calendar.getInstance().apply {
            time = medicine.startDate
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        // Only schedule if start date is today or in the future
        if (startDate.after(currentDate) || isSameDay(startDate, currentDate)) {
            medicine.reminderTimes.forEach { timeString ->
                scheduleReminderForTime(medicine, timeString, startDate)
            }
        }
    }
    
    /**
     * Lên lịch nhắc nhở cho một thời gian cụ thể
     */
    private fun scheduleReminderForTime(
        medicine: MedicineEntity,
        timeString: String,
        startDate: Calendar
    ) {
        val (hour, minute) = parseTimeString(timeString)
        
        val reminderTime = Calendar.getInstance().apply {
            time = startDate.time
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        val currentTime = Calendar.getInstance()
        
        // If reminder time is in the past, schedule for tomorrow
        if (reminderTime.before(currentTime) && !isSameDay(reminderTime, currentTime)) {
            reminderTime.add(Calendar.DAY_OF_YEAR, 1)
        }
        
        val delay = reminderTime.timeInMillis - currentTime.timeInMillis
        
        // Only schedule if delay is positive
        if (delay > 0) {
            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "medicineId" to medicine.id,
                        "medicineName" to medicine.name,
                        "reminderTime" to timeString,
                        "quantity" to medicine.quantity,
                        "unit" to medicine.unit,
                        "intakeAdvice" to medicine.intakeAdvice
                    )
                )
                .addTag("${Constants.REMINDER_WORK_NAME_PREFIX}${medicine.id}")
                .addTag(Constants.REMINDER_WORK_TAG)
                .build()
            
            workManager.enqueue(workRequest)
        }
    }
    
    /**
     * Hủy lịch nhắc nhở cho một thuốc
     */
    fun cancelReminder(medicineId: Long) {
        workManager.cancelAllWorkByTag("${Constants.REMINDER_WORK_NAME_PREFIX}$medicineId")
    }
    
    /**
     * Cập nhật lịch nhắc nhở cho một thuốc
     */
    fun updateReminder(medicine: MedicineEntity) {
        scheduleReminder(medicine)
    }
    
    /**
     * Parse time string (e.g., "10:00 AM") thành hour và minute
     */
    private fun parseTimeString(timeString: String): Pair<Int, Int> {
        return try {
            val parts = timeString.split(":")
            val hourPart = parts[0].trim().toInt()
            val minutePart = parts[1].split(" ")[0].trim().toInt()
            val isPM = timeString.contains("PM", ignoreCase = true)
            
            val hour24 = when {
                isPM && hourPart != 12 -> hourPart + 12
                !isPM && hourPart == 12 -> 0
                else -> hourPart
            }
            
            Pair(hour24, minutePart)
        } catch (e: Exception) {
            // Default to 9:00 AM if parsing fails
            Pair(9, 0)
        }
    }
    
    /**
     * Kiểm tra xem hai Calendar có cùng ngày không
     */
    private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }
}

