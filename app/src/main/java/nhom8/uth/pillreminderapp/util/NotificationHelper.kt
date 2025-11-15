package nhom8.uth.pillreminderapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import nhom8.uth.pillreminderapp.MainActivity
import nhom8.uth.pillreminderapp.receivers.ReminderActionReceiver
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper class để tạo và hiển thị notifications
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    
    init {
        createNotificationChannel()
    }
    
    /**
     * Tạo notification channel (cần cho Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
                // Set sound - sử dụng default notification sound
                setSound(
                    android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION),
                    android.media.AudioAttributes.Builder()
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }
            notificationManager.createNotificationChannel(channel)
            android.util.Log.d("NotificationHelper", "Notification channel created: ${Constants.NOTIFICATION_CHANNEL_ID}")
        }
    }
    
    /**
     * Hiển thị notification với action buttons (Confirm, Skip, Remind Later)
     */
    fun showReminderNotification(
        medicineName: String,
        quantity: Int,
        unit: String,
        reminderTime: String,
        medicineId: Long,
        notificationId: Int
    ) {
        android.util.Log.d("NotificationHelper", "showReminderNotification called: $medicineName, $reminderTime")
        
        // Format: "Medication at {time}" + "{medicineName} {quantity} {unit}"
        val title = "Medication at $reminderTime"
        val message = "$medicineName $quantity $unit"
        
        android.util.Log.d("NotificationHelper", "Title: $title, Message: $message")
        
        // Intent để mở app khi click vào notification
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Intent cho Confirm action
        val confirmIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = Constants.ACTION_CONFIRM
            putExtra("medicineId", medicineId)
            putExtra("reminderTime", reminderTime)
            putExtra("notificationId", notificationId)
        }
        
        val confirmPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            confirmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Intent cho Skip action
        val skipIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = Constants.ACTION_SKIP
            putExtra("medicineId", medicineId)
            putExtra("reminderTime", reminderTime)
            putExtra("notificationId", notificationId)
        }
        
        val skipPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 1000,
            skipIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Intent cho Remind Later action (30 minutes)
        val remindLaterIntent = Intent(context, ReminderActionReceiver::class.java).apply {
            action = Constants.ACTION_REMIND_LATER
            putExtra("medicineId", medicineId)
            putExtra("reminderTime", reminderTime)
            putExtra("notificationId", notificationId)
        }
        
        val remindLaterPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 2000,
            remindLaterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Replace with app icon
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true)
            .addAction(
                android.R.drawable.ic_menu_revert,
                "Confirm",
                confirmPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Skip",
                skipPendingIntent
            )
            .addAction(
                android.R.drawable.ic_menu_recent_history,
                "30 mins later",
                remindLaterPendingIntent
            )
            .build()
        
        android.util.Log.d("NotificationHelper", "Notifying with ID: $notificationId")
        notificationManager.notify(notificationId, notification)
        android.util.Log.d("NotificationHelper", "Notification sent successfully")
    }
    
    /**
     * Hiển thị notification (backward compatibility)
     */
    fun showNotification(
        title: String,
        message: String,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        // Intent để mở app khi click vào notification
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Replace with app icon
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(notificationId, notification)
    }
}
