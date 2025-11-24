package nhom8.uth.pillreminderapp.ui.screens.add_med

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import nhom8.uth.pillreminderapp.data.repository.MedicineRepository
import nhom8.uth.pillreminderapp.util.AlarmScheduler
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel quản lý logic cho màn hình thêm/sửa thuốc
 */
@HiltViewModel
class AddMedViewModel @Inject constructor(
    private val repository: MedicineRepository,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {
    
    // Form state
    private val _medicineName = MutableStateFlow("")
    val medicineName: StateFlow<String> = _medicineName.asStateFlow()
    
    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()
    
    private val _unit = MutableStateFlow("pills")
    val unit: StateFlow<String> = _unit.asStateFlow()
    
    private val _intakeAdvice = MutableStateFlow("None")
    val intakeAdvice: StateFlow<String> = _intakeAdvice.asStateFlow()
    
    private val _startDate = MutableStateFlow<Date>(Date())
    val startDate: StateFlow<Date> = _startDate.asStateFlow()
    
    private val _endDate = MutableStateFlow<Date?>(null)
    val endDate: StateFlow<Date?> = _endDate.asStateFlow()
    
    private val _reminderTimes = MutableStateFlow<List<String>>(emptyList())
    val reminderTimes: StateFlow<List<String>> = _reminderTimes.asStateFlow()
    
    private val _repeat = MutableStateFlow("Daily")
    val repeat: StateFlow<String> = _repeat.asStateFlow()
    
    private val _notes = MutableStateFlow<String?>(null)
    val notes: StateFlow<String?> = _notes.asStateFlow()
    
    // Medicine ID for edit mode
    private val _medicineId = MutableStateFlow<Long?>(null)
    val medicineId: StateFlow<Long?> = _medicineId.asStateFlow()
    
    // Validation errors
    private val _nameError = MutableStateFlow<String?>(null)
    val nameError: StateFlow<String?> = _nameError.asStateFlow()
    
    private val _reminderTimesError = MutableStateFlow<String?>(null)
    val reminderTimesError: StateFlow<String?> = _reminderTimesError.asStateFlow()
    
    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Save success state
    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess: StateFlow<Boolean> = _saveSuccess.asStateFlow()
    
    // Available options - using Constants
    val units = nhom8.uth.pillreminderapp.util.Constants.MEDICINE_UNITS
    val intakeAdviceOptions = nhom8.uth.pillreminderapp.util.Constants.INTAKE_ADVICE_OPTIONS
    val repeatOptions = nhom8.uth.pillreminderapp.util.Constants.REPEAT_OPTIONS
    
    /**
     * Load medicine data for edit mode
     */
    fun loadMedicine(medicineId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val medicine = repository.getMedicineById(medicineId)
                if (medicine != null) {
                    _medicineId.value = medicine.id
                    _medicineName.value = medicine.name
                    _quantity.value = medicine.quantity
                    _unit.value = medicine.unit
                    _intakeAdvice.value = medicine.intakeAdvice
                    _startDate.value = medicine.startDate
                    _endDate.value = medicine.endDate
                    _reminderTimes.value = medicine.reminderTimes
                    _repeat.value = medicine.repeat
                    _notes.value = medicine.notes
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Update medicine name
     */
    fun updateName(name: String) {
        _medicineName.value = name
        _nameError.value = null
    }
    
    /**
     * Update quantity
     */
    fun updateQuantity(quantity: Int) {
        if (quantity > 0) {
            _quantity.value = quantity
        }
    }
    
    /**
     * Update unit
     */
    fun updateUnit(unit: String) {
        _unit.value = unit
    }
    
    /**
     * Update intake advice
     */
    fun updateIntakeAdvice(advice: String) {
        _intakeAdvice.value = advice
    }
    
    /**
     * Update start date
     */
    fun updateStartDate(date: Date) {
        _startDate.value = date
        // If end date is before start date, clear it
        _endDate.value?.let { endDate ->
            if (endDate.before(date)) {
                _endDate.value = null
            }
        }
    }
    
    /**
     * Set start date to today
     */
    fun setStartDateToToday() {
        _startDate.value = Date()
    }
    
    /**
     * Update end date
     */
    fun updateEndDate(date: Date?) {
        // Validate that end date is after start date
        if (date != null && date.before(_startDate.value)) {
            return
        }
        _endDate.value = date
    }
    
    /**
     * Add reminder time
     */
    fun addReminderTime(time: String) {
        val currentTimes = _reminderTimes.value.toMutableList()
        if (!currentTimes.contains(time)) {
            currentTimes.add(time)
            _reminderTimes.value = currentTimes.sorted()
            _reminderTimesError.value = null
        }
    }
    
    /**
     * Remove reminder time
     */
    fun removeReminderTime(time: String) {
        val currentTimes = _reminderTimes.value.toMutableList()
        currentTimes.remove(time)
        _reminderTimes.value = currentTimes
    }
    
    /**
     * Update repeat option
     */
    fun updateRepeat(repeat: String) {
        _repeat.value = repeat
    }
    
    /**
     * Update notes
     */
    fun updateNotes(notes: String?) {
        _notes.value = notes?.takeIf { it.isNotBlank() }
    }
    
    /**
     * Validate form
     */
    private fun validateForm(): Boolean {
        var isValid = true
        
        // Validate name
        if (_medicineName.value.isBlank()) {
            _nameError.value = "Medicine name is required"
            isValid = false
        } else {
            _nameError.value = null
        }
        
        // Validate reminder times
        if (_reminderTimes.value.isEmpty()) {
            _reminderTimesError.value = "At least one reminder time is required"
            isValid = false
        } else {
            _reminderTimesError.value = null
        }
        
        return isValid
    }
    
    /**
     * Save medicine (create or update)
     */
    fun saveMedicine(onSuccess: () -> Unit) {
        if (!validateForm()) {
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val medicine = MedicineEntity(
                    id = _medicineId.value ?: 0L,
                    name = _medicineName.value.trim(),
                    quantity = _quantity.value,
                    unit = _unit.value,
                    intakeAdvice = _intakeAdvice.value,
                    startDate = _startDate.value,
                    endDate = _endDate.value,
                    reminderTimes = _reminderTimes.value,
                    repeat = _repeat.value,
                    notes = _notes.value,
                    isActive = true
                )
                
                val savedMedicineId = if (_medicineId.value != null) {
                    // Update existing medicine
                    repository.updateMedicine(medicine)
                    // Cancel old reminders and schedule new ones
                    alarmScheduler.cancelReminder(medicine.id)
                    medicine.id
                } else {
                    // Insert new medicine and get the generated ID
                    val newId = repository.insertMedicine(medicine)
                    newId
                }
                
                // Schedule reminders for the saved medicine with the correct ID
                val savedMedicine = medicine.copy(id = savedMedicineId)
                alarmScheduler.scheduleReminder(savedMedicine)
                
                android.util.Log.d("AddMedViewModel", "Saved medicine: ${savedMedicine.name}, ID: $savedMedicineId, Scheduled reminders")
                
                _saveSuccess.value = true
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: Show error message to user
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Reset form
     */
    fun resetForm() {
        _medicineId.value = null
        _medicineName.value = ""
        _quantity.value = 1
        _unit.value = "pills"
        _intakeAdvice.value = "None"
        _startDate.value = Date()
        _endDate.value = null
        _reminderTimes.value = emptyList()
        _repeat.value = "Daily"
        _notes.value = null
        _nameError.value = null
        _reminderTimesError.value = null
        _saveSuccess.value = false
    }
}
