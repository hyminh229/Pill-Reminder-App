package nhom8.uth.pillreminderapp.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import nhom8.uth.pillreminderapp.util.NotificationHelper

/**
 * Worker để xử lý nhắc nhở uống thuốc
 * Sử dụng WorkManager để đảm bảo thông báo được gửi đúng thời gian
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            android.util.Log.d("ReminderWorker", "doWork() called")
            
            // Lấy thông tin từ input data
            val medicineId = inputData.getLong("medicineId", -1L)
            val medicineName = inputData.getString("medicineName") ?: "Medicine"
            val reminderTime = inputData.getString("reminderTime") ?: ""
            val quantity = inputData.getInt("quantity", 1)
            val unit = inputData.getString("unit") ?: "pills"
            
            android.util.Log.d("ReminderWorker", "Medicine: $medicineName, Time: $reminderTime, ID: $medicineId")
            
            // Sử dụng notificationId unique để tránh conflict
            val notificationId = (medicineId.toString() + reminderTime.replace(":", "").replace(" ", "")).hashCode()
            
            android.util.Log.d("ReminderWorker", "Showing notification with ID: $notificationId")
            
            // Hiển thị notification với action buttons
            notificationHelper.showReminderNotification(
                medicineName = medicineName,
                quantity = quantity,
                unit = unit,
                reminderTime = reminderTime,
                medicineId = medicineId,
                notificationId = notificationId
            )
            
            android.util.Log.d("ReminderWorker", "Notification shown successfully")
            
            Result.success()
        } catch (e: Exception) {
            android.util.Log.e("ReminderWorker", "Error in doWork()", e)
            e.printStackTrace()
            Result.retry()
        }
    }
}

