package nhom8.uth.pillreminderapp.ui.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color as ComposeColor
import nhom8.uth.pillreminderapp.data.database.dao.MedicineStatistics
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import nhom8.uth.pillreminderapp.data.repository.MedicineRepository
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

/**
 * Data class cho chart item (hiển thị trong donut chart)
 */
data class ChartItem(
    val medicineName: String,
    val value: Int,
    val percentage: Float,
    val color: ComposeColor
)

/**
 * Data class cho history item (hiển thị trong list)
 */
data class HistoryItem(
    val history: HistoryEntity,
    val medicine: MedicineEntity?
)

/**
 * Enum cho filter thời gian
 */
enum class TimeFilter {
    WEEK, MONTH, YEAR, ALL
}

/**
 * ViewModel quản lý logic cho màn hình Statistics
 */
@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val repository: MedicineRepository
) : ViewModel() {
    
    // StateFlow cho chart data
    private val _chartData = MutableStateFlow<List<ChartItem>>(emptyList())
    val chartData: StateFlow<List<ChartItem>> = _chartData.asStateFlow()
    
    // StateFlow cho total value (hiển thị ở giữa chart)
    private val _totalValue = MutableStateFlow(0)
    val totalValue: StateFlow<Int> = _totalValue.asStateFlow()
    
    // StateFlow cho history list
    private val _historyList = MutableStateFlow<List<HistoryItem>>(emptyList())
    val historyList: StateFlow<List<HistoryItem>> = _historyList.asStateFlow()
    
    // StateFlow cho time filter
    private val _timeFilter = MutableStateFlow(TimeFilter.ALL)
    val timeFilter: StateFlow<TimeFilter> = _timeFilter.asStateFlow()
    
    // StateFlow cho loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    // Colors cho chart segments
    private val chartColors = listOf(
        ComposeColor(0xFF26A69A), // Teal
        ComposeColor(0xFFFFEB3B), // Yellow
        ComposeColor(0xFFFF9800), // Orange
        ComposeColor(0xFFEC407A), // Pink
        ComposeColor(0xFF9C27B0), // Purple
        ComposeColor(0xFF2196F3), // Blue
    )
    
    init {
        loadStatistics()
    }
    
    /**
     * Load statistics và history dựa trên time filter hiện tại
     */
    fun loadStatistics() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val (startDate, endDate) = getDateRange(_timeFilter.value)
                
                // Load statistics by medicine
                val medicineStats = repository.getStatisticsByMedicine(startDate, endDate)
                
                // Load all medicines để map với statistics
                val allMedicines = repository.getAllMedicines().first()
                val medicineMap = allMedicines.associateBy { it.id }
                
                // Tạo chart data
                val chartItems = medicineStats.mapIndexed { index, stat ->
                    val medicine = medicineMap[stat.medicineId]
                    val medicineName = medicine?.name ?: "Unknown"
                    val total = stat.total
                    val percentage = if (total > 0) (stat.taken.toFloat() / total) * 100f else 0f
                    val color = chartColors.getOrElse(index % chartColors.size) { chartColors[0] }
                    
                    ChartItem(
                        medicineName = medicineName,
                        value = stat.taken,
                        percentage = percentage,
                        color = color
                    )
                }.sortedByDescending { it.value }
                
                // Tính total value (tổng số taken)
                val total = chartItems.sumOf { it.value }
                
                _chartData.value = chartItems
                _totalValue.value = total
                
                // Load history list
                loadHistoryList(startDate, endDate, medicineMap)
                
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Load history list
     */
    private suspend fun loadHistoryList(
        startDate: Date,
        endDate: Date,
        medicineMap: Map<Long, MedicineEntity>
    ) {
        val histories = repository.getHistoryByDateRange(startDate, endDate)
        val historyItems = histories.map { history ->
            HistoryItem(
                history = history,
                medicine = medicineMap[history.medicineId]
            )
        }
        _historyList.value = historyItems
    }
    
    /**
     * Update time filter và reload data
     */
    fun setTimeFilter(filter: TimeFilter) {
        _timeFilter.value = filter
        loadStatistics()
    }
    
    /**
     * Tính toán date range dựa trên time filter
     */
    private fun getDateRange(filter: TimeFilter): Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        
        when (filter) {
            TimeFilter.WEEK -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
            }
            TimeFilter.MONTH -> {
                calendar.add(Calendar.MONTH, -1)
            }
            TimeFilter.YEAR -> {
                calendar.add(Calendar.YEAR, -1)
            }
            TimeFilter.ALL -> {
                // Lấy tất cả data (set start date rất xa trong quá khứ)
                calendar.set(Calendar.YEAR, 2020)
                calendar.set(Calendar.MONTH, Calendar.JANUARY)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }
        }
        
        val startDate = calendar.time
        return Pair(startDate, endDate)
    }
}
