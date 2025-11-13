package nhom8.uth.pillreminderapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import nhom8.uth.pillreminderapp.data.database.converter.Converters
import nhom8.uth.pillreminderapp.data.database.dao.HistoryDao
import nhom8.uth.pillreminderapp.data.database.dao.MedicineDao
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity

/**
 * Room Database cho ứng dụng Pill Reminder
 * Quản lý các bảng: medicines và history
 */
@Database(
    entities = [MedicineEntity::class, HistoryEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun medicineDao(): MedicineDao
    abstract fun historyDao(): HistoryDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        private const val DATABASE_NAME = "pill_reminder_database"
        
        /**
         * Lấy instance của database (Singleton pattern)
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration() // Xóa và tạo lại database khi version thay đổi (chỉ dùng trong development)
                    .build()
                INSTANCE = instance
                instance
            }
        }
        
        /**
         * Migration từ version 1 lên version 2 (ví dụ cho tương lai)
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Thêm các migration logic ở đây khi cần update schema
                // Ví dụ: thêm cột mới, thay đổi kiểu dữ liệu, etc.
            }
        }
    }
}
