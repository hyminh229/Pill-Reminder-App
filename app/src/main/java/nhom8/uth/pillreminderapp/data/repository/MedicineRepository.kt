package nhom8.uth.pillreminderapp.data.repository

import kotlinx.coroutines.flow.Flow
import nhom8.uth.pillreminderapp.data.database.dao.HistoryDao
import nhom8.uth.pillreminderapp.data.database.dao.MedicineStatistics
import nhom8.uth.pillreminderapp.data.database.dao.OverallStatistics
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import java.util.Date

/**
 * Repository interface cho Medicine và History
 * Cung cấp abstraction layer giữa ViewModel và Data Source
 */
interface MedicineRepository {
    
    // ========== Medicine Operations ==========
    
    /**
     * Lấy tất cả các thuốc
     */
    fun getAllMedicines(): Flow<List<MedicineEntity>>
    
    /**
     * Lấy tất cả các thuốc đang hoạt động
     */
    fun getActiveMedicines(): Flow<List<MedicineEntity>>
    
    /**
     * Lấy thuốc theo ID
     */
    suspend fun getMedicineById(id: Long): MedicineEntity?
    
    /**
     * Lấy các thuốc cần nhắc nhở trong một ngày cụ thể
     */
    suspend fun getMedicinesByDate(date: Date): List<MedicineEntity>
    
    /**
     * Lấy các thuốc quá hạn (overdue)
     */
    suspend fun getOverdueMedicines(currentDate: Date): List<MedicineEntity>
    
    /**
     * Insert một thuốc mới
     */
    suspend fun insertMedicine(medicine: MedicineEntity): Long
    
    /**
     * Insert nhiều thuốc
     */
    suspend fun insertAllMedicines(medicines: List<MedicineEntity>)
    
    /**
     * Update thuốc
     */
    suspend fun updateMedicine(medicine: MedicineEntity)
    
    /**
     * Delete thuốc
     */
    suspend fun deleteMedicine(medicine: MedicineEntity)
    
    /**
     * Delete thuốc theo ID
     */
    suspend fun deleteMedicineById(id: Long)
    
    /**
     * Đánh dấu thuốc là không hoạt động (soft delete)
     */
    suspend fun deactivateMedicine(id: Long)
    
    /**
     * Đánh dấu thuốc là hoạt động
     */
    suspend fun activateMedicine(id: Long)
    
    // ========== History Operations ==========
    
    /**
     * Insert một lịch sử mới
     */
    suspend fun insertHistory(history: HistoryEntity): Long
    
    /**
     * Insert nhiều lịch sử
     */
    suspend fun insertAllHistories(histories: List<HistoryEntity>)
    
    /**
     * Lấy lịch sử theo ID thuốc
     */
    fun getHistoryByMedicineId(medicineId: Long): Flow<List<HistoryEntity>>
    
    /**
     * Lấy lịch sử trong khoảng thời gian
     */
    suspend fun getHistoryByDateRange(startDate: Date, endDate: Date): List<HistoryEntity>
    
    /**
     * Lấy lịch sử hôm nay
     */
    suspend fun getTodayHistory(startOfDay: Date, endOfDay: Date): List<HistoryEntity>
    
    /**
     * Lấy lịch sử hôm nay dưới dạng Flow
     */
    fun getTodayHistoryFlow(startOfDay: Date, endOfDay: Date): Flow<List<HistoryEntity>>
    
    /**
     * Lấy tất cả lịch sử
     */
    fun getAllHistory(): Flow<List<HistoryEntity>>
    
    /**
     * Lấy thống kê theo thuốc
     */
    suspend fun getStatisticsByMedicine(startDate: Date, endDate: Date): List<MedicineStatistics>
    
    /**
     * Lấy thống kê tổng hợp
     */
    suspend fun getOverallStatistics(startDate: Date, endDate: Date): OverallStatistics?
    
    /**
     * Delete lịch sử theo ID
     */
    suspend fun deleteHistory(id: Long)
    
    /**
     * Delete tất cả lịch sử của một thuốc
     */
    suspend fun deleteHistoryByMedicineId(medicineId: Long)
    
    /**
     * Update trạng thái lịch sử
     */
    suspend fun updateHistory(history: HistoryEntity)
    
    /**
     * Đánh dấu thuốc đã uống (tạo history entry)
     */
    suspend fun markMedicineAsTaken(medicineId: Long, date: Date, time: String)
    
    /**
     * Đánh dấu thuốc đã bỏ lỡ
     */
    suspend fun markMedicineAsMissed(medicineId: Long, date: Date, time: String)
    
    /**
     * Đánh dấu thuốc đã bỏ qua
     */
    suspend fun markMedicineAsSkipped(medicineId: Long, date: Date, time: String)
}
