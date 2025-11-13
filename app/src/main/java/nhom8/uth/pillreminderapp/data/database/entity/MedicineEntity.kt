package nhom8.uth.pillreminderapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import nhom8.uth.pillreminderapp.data.database.converter.Converters
import java.util.Date

/**
 * Entity đại diện cho thông tin thuốc trong database
 */
@Entity(tableName = "medicines")
@TypeConverters(Converters::class)
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String, // Tên thuốc
    
    val quantity: Int, // Số lượng (ví dụ: 2)
    
    val unit: String, // Đơn vị (pills, ampoule, application, drop, gram, injection, miligram, teaspoon)
    
    val intakeAdvice: String, // Lời khuyên uống (None, Before meal, With meal, After meal)
    
    val startDate: Date, // Ngày bắt đầu
    
    val endDate: Date?, // Ngày kết thúc (nullable)
    
    val reminderTimes: List<String>, // Danh sách các thời điểm nhắc nhở (ví dụ: ["10:00 AM", "06:00 PM"])
    
    val repeat: String, // Lặp lại (Daily, Weekly, Custom)
    
    val notes: String? = null, // Ghi chú (optional)
    
    val isActive: Boolean = true // Trạng thái hoạt động
)
