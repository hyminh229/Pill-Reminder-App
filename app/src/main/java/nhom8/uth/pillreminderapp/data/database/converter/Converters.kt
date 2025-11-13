package nhom8.uth.pillreminderapp.data.database.converter

import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters cho Room Database
 * Chuyển đổi các kiểu dữ liệu không được Room hỗ trợ trực tiếp
 */
class Converters {
    
    /**
     * Chuyển đổi Date thành Long (timestamp)
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    /**
     * Chuyển đổi Long (timestamp) thành Date
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    /**
     * Chuyển đổi List<String> thành String (comma-separated)
     */
    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        return value?.split(",")?.map { it.trim() } ?: emptyList()
    }
    
    /**
     * Chuyển đổi String (comma-separated) thành List<String>
     */
    @TypeConverter
    fun toStringList(list: List<String>?): String {
        return list?.joinToString(",") ?: ""
    }
}

