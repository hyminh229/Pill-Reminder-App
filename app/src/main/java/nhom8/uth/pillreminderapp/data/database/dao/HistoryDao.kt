package nhom8.uth.pillreminderapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import java.util.Date

/**
 * Data Access Object cho HistoryEntity
 * Cung cấp các operations để thao tác với bảng history
 */
@Dao
interface HistoryDao {
    
    /**
     * Insert một lịch sử mới
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: HistoryEntity): Long
    
    /**
     * Insert nhiều lịch sử
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(histories: List<HistoryEntity>)
    
    /**
     * Lấy lịch sử theo ID thuốc
     */
    @Query("SELECT * FROM history WHERE medicineId = :medicineId ORDER BY takenDate DESC, takenTime DESC")
    fun getHistoryByMedicineId(medicineId: Long): Flow<List<HistoryEntity>>
    
    /**
     * Lấy lịch sử trong khoảng thời gian
     */
    @Query("""
        SELECT * FROM history 
        WHERE takenDate >= :startDate AND takenDate <= :endDate 
        ORDER BY takenDate DESC, takenTime DESC
    """)
    suspend fun getHistoryByDateRange(startDate: Date, endDate: Date): List<HistoryEntity>
    
    /**
     * Lấy lịch sử hôm nay
     * Sử dụng timestamp để so sánh ngày (bỏ qua phần giờ)
     */
    @Query("""
        SELECT * FROM history 
        WHERE takenDate >= :startOfDay AND takenDate < :endOfDay
        ORDER BY takenTime DESC
    """)
    suspend fun getTodayHistory(startOfDay: Date, endOfDay: Date): List<HistoryEntity>
    
    /**
     * Lấy lịch sử hôm nay dưới dạng Flow
     */
    @Query("""
        SELECT * FROM history 
        WHERE takenDate >= :startOfDay AND takenDate < :endOfDay
        ORDER BY takenTime DESC
    """)
    fun getTodayHistoryFlow(startOfDay: Date, endOfDay: Date): Flow<List<HistoryEntity>>
    
    /**
     * Lấy tất cả lịch sử
     */
    @Query("SELECT * FROM history ORDER BY takenDate DESC, takenTime DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>
    
    /**
     * Lấy thống kê theo thuốc
     * Trả về số lượng taken, missed, skipped cho mỗi thuốc
     */
    @Query("""
        SELECT 
            medicineId,
            COUNT(*) as total,
            SUM(CASE WHEN status = 'taken' THEN 1 ELSE 0 END) as taken,
            SUM(CASE WHEN status = 'missed' THEN 1 ELSE 0 END) as missed,
            SUM(CASE WHEN status = 'skipped' THEN 1 ELSE 0 END) as skipped
        FROM history
        WHERE takenDate >= :startDate AND takenDate <= :endDate
        GROUP BY medicineId
    """)
    suspend fun getStatisticsByMedicine(
        startDate: Date, 
        endDate: Date
    ): List<MedicineStatisticsRaw>
    
    /**
     * Lấy thống kê tổng hợp
     */
    @Query("""
        SELECT 
            COUNT(*) as total,
            SUM(CASE WHEN status = 'taken' THEN 1 ELSE 0 END) as taken,
            SUM(CASE WHEN status = 'missed' THEN 1 ELSE 0 END) as missed,
            SUM(CASE WHEN status = 'skipped' THEN 1 ELSE 0 END) as skipped
        FROM history
        WHERE takenDate >= :startDate AND takenDate <= :endDate
    """)
    suspend fun getOverallStatistics(
        startDate: Date, 
        endDate: Date
    ): OverallStatisticsRaw?
    
    /**
     * Delete lịch sử theo ID
     */
    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteHistory(id: Long)
    
    /**
     * Delete tất cả lịch sử của một thuốc
     */
    @Query("DELETE FROM history WHERE medicineId = :medicineId")
    suspend fun deleteHistoryByMedicineId(medicineId: Long)
    
    /**
     * Update trạng thái lịch sử
     */
    @Update
    suspend fun updateHistory(history: HistoryEntity)
}

/**
 * Data class cho thống kê theo thuốc (raw từ Room query)
 */
data class MedicineStatisticsRaw(
    val medicineId: Long,
    val total: Int,
    val taken: Int,
    val missed: Int,
    val skipped: Int
) {
    fun toMedicineStatistics(): MedicineStatistics {
        return MedicineStatistics(medicineId, total, taken, missed, skipped)
    }
}

/**
 * Data class cho thống kê tổng hợp (raw từ Room query)
 */
data class OverallStatisticsRaw(
    val total: Int,
    val taken: Int,
    val missed: Int,
    val skipped: Int
) {
    fun toOverallStatistics(): OverallStatistics {
        return OverallStatistics(total, taken, missed, skipped)
    }
}

/**
 * Data class cho thống kê theo thuốc
 */
data class MedicineStatistics(
    val medicineId: Long,
    val total: Int,
    val taken: Int,
    val missed: Int,
    val skipped: Int
)

/**
 * Data class cho thống kê tổng hợp
 */
data class OverallStatistics(
    val total: Int,
    val taken: Int,
    val missed: Int,
    val skipped: Int
)
