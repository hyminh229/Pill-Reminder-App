package nhom8.uth.pillreminderapp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class PillReminderApplication : Application() {
    
    @dagger.hilt.EntryPoint
    @dagger.hilt.InstallIn(SingletonComponent::class)
    interface WorkerFactoryEntryPoint {
        fun hiltWorkerFactory(): HiltWorkerFactory
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize WorkManager với HiltWorkerFactory
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            WorkerFactoryEntryPoint::class.java
        )
        
        val configuration = Configuration.Builder()
            .setWorkerFactory(entryPoint.hiltWorkerFactory())
            .build()
        
        WorkManager.initialize(this, configuration)
        
        android.util.Log.d("PillReminderApplication", "WorkManager initialized with HiltWorkerFactory")
    }
}
