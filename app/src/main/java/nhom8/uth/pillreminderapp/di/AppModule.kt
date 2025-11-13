package nhom8.uth.pillreminderapp.di

import android.content.Context
import androidx.work.WorkManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import nhom8.uth.pillreminderapp.data.AppDatabase
import nhom8.uth.pillreminderapp.data.database.dao.HistoryDao
import nhom8.uth.pillreminderapp.data.database.dao.MedicineDao
import nhom8.uth.pillreminderapp.data.repository.MedicineRepository
import nhom8.uth.pillreminderapp.data.repository.MedicineRepositoryImpl
import nhom8.uth.pillreminderapp.util.PreferencesManager
import javax.inject.Singleton

/**
 * Hilt Module cung cấp các dependencies cho ứng dụng
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    
    /**
     * Binds MedicineRepository interface với implementation
     * MedicineRepositoryImpl đã có @Inject constructor, Hilt sẽ tự động inject
     */
    @Binds
    @Singleton
    abstract fun bindMedicineRepository(
        medicineRepositoryImpl: MedicineRepositoryImpl
    ): MedicineRepository
    
    companion object {
        /**
         * Provides AppDatabase instance
         */
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return AppDatabase.getDatabase(context)
        }
        
        /**
         * Provides MedicineDao
         */
        @Provides
        fun provideMedicineDao(database: AppDatabase): MedicineDao {
            return database.medicineDao()
        }
        
        /**
         * Provides HistoryDao
         */
        @Provides
        fun provideHistoryDao(database: AppDatabase): HistoryDao {
            return database.historyDao()
        }
        
        /**
         * Provides WorkManager instance
         */
        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
        
        /**
         * Provides PreferencesManager
         */
        @Provides
        @Singleton
        fun providePreferencesManager(@ApplicationContext context: Context): PreferencesManager {
            return PreferencesManager(context)
        }
    }
}
