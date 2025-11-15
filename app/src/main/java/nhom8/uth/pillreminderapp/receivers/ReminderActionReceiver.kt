package nhom8.uth.pillreminderapp.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nhom8.uth.pillreminderapp.PillReminderApplication
import nhom8.uth.pillreminderapp.data.repository.MedicineRepository
import nhom8.uth.pillreminderapp.util.AlarmScheduler
import nhom8.uth.pillreminderapp.util.Constants
import java.util.Date

/**
 * BroadcastReceiver để xử lý các action từ notification (Confirm, Skip)
 */
class ReminderActionReceiver : BroadcastReceiver() {
    
    @dagger.hilt.EntryPoint
    @dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
    interface ReminderActionEntryPoint {
        fun medicineRepository(): MedicineRepository
        fun alarmScheduler(): AlarmScheduler
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val medicineId = intent.getLongExtra("medicineId", -1L)
        val reminderTime = intent.getStringExtra("reminderTime") ?: ""
        val notificationId = intent.getIntExtra("notificationId", -1)
        
        if (medicineId == -1L) return
        
        // Lấy dependencies từ Hilt
        val app = context.applicationContext as? PillReminderApplication
        if (app == null) return
        
        val entryPoint = EntryPointAccessors.fromApplication(
            app,
            ReminderActionEntryPoint::class.java
        )
        
        val repository = entryPoint.medicineRepository()
        val alarmScheduler = entryPoint.alarmScheduler()
        
        when (action) {
            Constants.ACTION_CONFIRM -> {
                // Confirm: Đánh dấu là taken
                CoroutineScope(Dispatchers.IO).launch {
                    val currentDate = Date()
                    repository.markMedicineAsTaken(medicineId, currentDate, reminderTime)
                }
                
                // Dismiss notification
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.cancel(notificationId)
            }
            
            Constants.ACTION_SKIP -> {
                // Skip: Đánh dấu là skipped
                CoroutineScope(Dispatchers.IO).launch {
                    val currentDate = Date()
                    repository.markMedicineAsSkipped(medicineId, currentDate, reminderTime)
                }
                
                // Dismiss notification
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.cancel(notificationId)
            }
            
            Constants.ACTION_REMIND_LATER -> {
                // Remind later: Thông báo lại sau 30 phút
                CoroutineScope(Dispatchers.IO).launch {
                    val medicine = repository.getMedicineById(medicineId)
                    if (medicine != null) {
                        alarmScheduler.scheduleReminderIn30Minutes(medicine, reminderTime)
                    }
                }
                
                // Dismiss notification
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
                notificationManager.cancel(notificationId)
            }
        }
    }
}

