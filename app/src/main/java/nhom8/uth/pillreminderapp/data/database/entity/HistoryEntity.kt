package nhom8.uth.pillreminderapp.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entity đại diện cho lịch sử uống thuốc
 */
@Entity(
    tableName = "history",
    foreignKeys = [
        ForeignKey(
            entity = MedicineEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicineId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medicineId"]), Index(value = ["takenDate"])]
)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val medicineId: Long, // ID của thuốc (foreign key)
    
    val takenDate: Date, // Ngày uống thuốc
    
    val takenTime: String, // Thời gian uống (ví dụ: "10:00 AM")
    
    val status: String // Trạng thái: "taken", "missed", "skipped"
) {
    companion object {
        const val STATUS_TAKEN = "taken"
        const val STATUS_MISSED = "missed"
        const val STATUS_SKIPPED = "skipped"
    }
}
