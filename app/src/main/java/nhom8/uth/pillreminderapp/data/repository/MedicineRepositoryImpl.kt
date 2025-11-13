package nhom8.uth.pillreminderapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import nhom8.uth.pillreminderapp.data.database.dao.HistoryDao
import nhom8.uth.pillreminderapp.data.database.dao.MedicineDao
import nhom8.uth.pillreminderapp.data.database.dao.MedicineStatistics
import nhom8.uth.pillreminderapp.data.database.dao.MedicineStatisticsRaw
import nhom8.uth.pillreminderapp.data.database.dao.OverallStatistics
import nhom8.uth.pillreminderapp.data.database.dao.OverallStatisticsRaw
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation của MedicineRepository
 * Sử dụng MedicineDao và HistoryDao để thao tác với database
 */
@Singleton
class MedicineRepositoryImpl @Inject constructor(
    private val medicineDao: MedicineDao,
    private val historyDao: HistoryDao
) : MedicineRepository {
    
    // ========== Medicine Operations ==========
    
    override fun getAllMedicines(): Flow<List<MedicineEntity>> {
        return medicineDao.getAllMedicines()
    }
    
    override fun getActiveMedicines(): Flow<List<MedicineEntity>> {
        return medicineDao.getActiveMedicines()
    }
    
    override suspend fun getMedicineById(id: Long): MedicineEntity? {
        return medicineDao.getMedicineById(id)
    }
    
    override suspend fun getMedicinesByDate(date: Date): List<MedicineEntity> {
        return medicineDao.getMedicinesByDate(date)
    }
    
    override suspend fun getOverdueMedicines(currentDate: Date): List<MedicineEntity> {
        return medicineDao.getOverdueMedicines(currentDate)
    }
    
    override suspend fun insertMedicine(medicine: MedicineEntity): Long {
        return medicineDao.insert(medicine)
    }
    
    override suspend fun insertAllMedicines(medicines: List<MedicineEntity>) {
        medicineDao.insertAll(medicines)
    }
    
    override suspend fun updateMedicine(medicine: MedicineEntity) {
        medicineDao.update(medicine)
    }
    
    override suspend fun deleteMedicine(medicine: MedicineEntity) {
        medicineDao.delete(medicine)
    }
    
    override suspend fun deleteMedicineById(id: Long) {
        medicineDao.deleteById(id)
    }
    
    override suspend fun deactivateMedicine(id: Long) {
        medicineDao.deactivateMedicine(id)
    }
    
    override suspend fun activateMedicine(id: Long) {
        medicineDao.activateMedicine(id)
    }
    
    // ========== History Operations ==========
    
    override suspend fun insertHistory(history: HistoryEntity): Long {
        return historyDao.insertHistory(history)
    }
    
    override suspend fun insertAllHistories(histories: List<HistoryEntity>) {
        historyDao.insertAll(histories)
    }
    
    override fun getHistoryByMedicineId(medicineId: Long): Flow<List<HistoryEntity>> {
        return historyDao.getHistoryByMedicineId(medicineId)
    }
    
    override suspend fun getHistoryByDateRange(
        startDate: Date,
        endDate: Date
    ): List<HistoryEntity> {
        return historyDao.getHistoryByDateRange(startDate, endDate)
    }
    
    override suspend fun getTodayHistory(startOfDay: Date, endOfDay: Date): List<HistoryEntity> {
        return historyDao.getTodayHistory(startOfDay, endOfDay)
    }
    
    override fun getTodayHistoryFlow(
        startOfDay: Date,
        endOfDay: Date
    ): Flow<List<HistoryEntity>> {
        return historyDao.getTodayHistoryFlow(startOfDay, endOfDay)
    }
    
    override fun getAllHistory(): Flow<List<HistoryEntity>> {
        return historyDao.getAllHistory()
    }
    
    override suspend fun getStatisticsByMedicine(
        startDate: Date,
        endDate: Date
    ): List<MedicineStatistics> {
        val rawStats = historyDao.getStatisticsByMedicine(startDate, endDate)
        return rawStats.map { it.toMedicineStatistics() }
    }
    
    override suspend fun getOverallStatistics(
        startDate: Date,
        endDate: Date
    ): OverallStatistics? {
        val rawStats = historyDao.getOverallStatistics(startDate, endDate)
        return rawStats?.toOverallStatistics()
    }
    
    override suspend fun deleteHistory(id: Long) {
        historyDao.deleteHistory(id)
    }
    
    override suspend fun deleteHistoryByMedicineId(medicineId: Long) {
        historyDao.deleteHistoryByMedicineId(medicineId)
    }
    
    override suspend fun updateHistory(history: HistoryEntity) {
        historyDao.updateHistory(history)
    }
    
    // ========== Convenience Methods ==========
    
    override suspend fun markMedicineAsTaken(medicineId: Long, date: Date, time: String) {
        val history = HistoryEntity(
            medicineId = medicineId,
            takenDate = date,
            takenTime = time,
            status = HistoryEntity.STATUS_TAKEN
        )
        insertHistory(history)
    }
    
    override suspend fun markMedicineAsMissed(medicineId: Long, date: Date, time: String) {
        val history = HistoryEntity(
            medicineId = medicineId,
            takenDate = date,
            takenTime = time,
            status = HistoryEntity.STATUS_MISSED
        )
        insertHistory(history)
    }
    
    override suspend fun markMedicineAsSkipped(medicineId: Long, date: Date, time: String) {
        val history = HistoryEntity(
            medicineId = medicineId,
            takenDate = date,
            takenTime = time,
            status = HistoryEntity.STATUS_SKIPPED
        )
        insertHistory(history)
    }
}
