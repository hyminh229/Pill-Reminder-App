package nhom8.uth.pillreminderapp.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import nhom8.uth.pillreminderapp.util.Constants
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
            // Lấy thông tin từ input data
            val medicineId = inputData.getLong("medicineId", -1L)
            val medicineName = inputData.getString("medicineName") ?: "Medicine"
            val reminderTime = inputData.getString("reminderTime") ?: ""
            val quantity = inputData.getInt("quantity", 1)
            val unit = inputData.getString("unit") ?: "pills"
            val intakeAdvice = inputData.getString("intakeAdvice") ?: "None"
            
            // Tạo notification
            val notificationTitle = "Time to take your medicine"
            val notificationMessage = buildString {
                append("Take $quantity $unit of $medicineName")
                if (intakeAdvice != "None") {
                    append(" ($intakeAdvice)")
                }
                if (reminderTime.isNotEmpty()) {
                    append(" at $reminderTime")
                }
            }
            
            // Hiển thị notification
            notificationHelper.showNotification(
                title = notificationTitle,
                message = notificationMessage,
                notificationId = medicineId.toInt()
            )
            
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
