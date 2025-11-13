package nhom8.uth.pillreminderapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import java.util.Date

/**
 * Data Access Object cho MedicineEntity
 * Cung cấp các operations để thao tác với bảng medicines
 */
@Dao
interface MedicineDao {
    
    /**
     * Lấy tất cả các thuốc
     */
    @Query("SELECT * FROM medicines ORDER BY name ASC")
    fun getAllMedicines(): Flow<List<MedicineEntity>>
    
    /**
     * Lấy tất cả các thuốc đang hoạt động
     */
    @Query("SELECT * FROM medicines WHERE isActive = 1 ORDER BY name ASC")
    fun getActiveMedicines(): Flow<List<MedicineEntity>>
    
    /**
     * Lấy thuốc theo ID
     */
    @Query("SELECT * FROM medicines WHERE id = :id")
    suspend fun getMedicineById(id: Long): MedicineEntity?
    
    /**
     * Lấy các thuốc cần nhắc nhở trong một ngày cụ thể
     * Kiểm tra xem ngày đó có nằm trong khoảng startDate và endDate không
     */
    @Query("""
        SELECT * FROM medicines 
        WHERE isActive = 1 
        AND startDate <= :date 
        AND (endDate IS NULL OR endDate >= :date)
        ORDER BY name ASC
    """)
    suspend fun getMedicinesByDate(date: Date): List<MedicineEntity>
    
    /**
     * Lấy các thuốc quá hạn (overdue)
     * Thuốc có reminder time đã qua nhưng chưa được đánh dấu là taken
     */
    @Query("""
        SELECT DISTINCT m.* FROM medicines m
        WHERE m.isActive = 1
        AND m.startDate <= :currentDate
        AND (m.endDate IS NULL OR m.endDate >= :currentDate)
    """)
    suspend fun getOverdueMedicines(currentDate: Date): List<MedicineEntity>
    
    /**
     * Insert một thuốc mới
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: MedicineEntity): Long
    
    /**
     * Insert nhiều thuốc
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>)
    
    /**
     * Update thuốc
     */
    @Update
    suspend fun update(medicine: MedicineEntity)
    
    /**
     * Delete thuốc
     */
    @Delete
    suspend fun delete(medicine: MedicineEntity)
    
    /**
     * Delete thuốc theo ID
     */
    @Query("DELETE FROM medicines WHERE id = :id")
    suspend fun deleteById(id: Long)
    
    /**
     * Đánh dấu thuốc là không hoạt động (soft delete)
     */
    @Query("UPDATE medicines SET isActive = 0 WHERE id = :id")
    suspend fun deactivateMedicine(id: Long)
    
    /**
     * Đánh dấu thuốc là hoạt động
     */
    @Query("UPDATE medicines SET isActive = 1 WHERE id = :id")
    suspend fun activateMedicine(id: Long)
}
