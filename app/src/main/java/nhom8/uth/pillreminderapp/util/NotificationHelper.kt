package nhom8.uth.pillreminderapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
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
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager,
    private val soundHelper: SoundHelper
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
        updateNotificationChannel()
    }
    
    /**
     * Cập nhật notification channel với sound từ preferences
     * Trên Android 8.0+, cần xóa và tạo lại channel để thay đổi sound
     */
    fun updateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = getSoundUri()
            android.util.Log.d("NotificationHelper", "=== Updating notification channel ===")
            android.util.Log.d("NotificationHelper", "Sound URI from preferences: $soundUri")
            
            // Kiểm tra channel hiện tại
            val existingChannel = notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID)
            if (existingChannel != null) {
                android.util.Log.d("NotificationHelper", "Existing channel found - Sound: ${existingChannel.sound}")
                android.util.Log.d("NotificationHelper", "Existing channel - Importance: ${existingChannel.importance}")
                
                // Chỉ xóa và tạo lại nếu sound khác
                if (existingChannel.sound != soundUri) {
                    android.util.Log.d("NotificationHelper", "Sound changed, deleting old channel...")
                    try {
                        notificationManager.deleteNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID)
                        android.util.Log.d("NotificationHelper", "Old channel deleted")
                    } catch (e: Exception) {
                        android.util.Log.e("NotificationHelper", "Error deleting channel: ${e.message}")
                    }
                } else {
                    android.util.Log.d("NotificationHelper", "Sound unchanged, no update needed")
                    return
                }
            } else {
                android.util.Log.d("NotificationHelper", "No existing channel found, creating new one")
            }
            
            // Tạo channel mới với sound từ preferences
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
                enableVibration(true)
                enableLights(true)
                setShowBadge(true)
                // Set sound từ preferences
                setSound(
                    soundUri,
                    android.media.AudioAttributes.Builder()
                        .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                        .build()
                )
            }
            notificationManager.createNotificationChannel(channel)
            android.util.Log.d("NotificationHelper", "New channel created with sound: $soundUri")
            
            // Verify channel was created correctly
            val createdChannel = notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID)
            if (createdChannel != null) {
                android.util.Log.d("NotificationHelper", "Channel verified - Sound URI: ${createdChannel.sound}")
                android.util.Log.d("NotificationHelper", "Channel verified - Importance: ${createdChannel.importance}")
            } else {
                android.util.Log.e("NotificationHelper", "ERROR: Failed to create channel!")
            }
            android.util.Log.d("NotificationHelper", "=== Channel update complete ===")
        } else {
            android.util.Log.d("NotificationHelper", "Android version < 8.0, no channel update needed")
        }
    }
    
    /**
     * Lấy URI của sound từ preferences
     */
    private fun getSoundUri(): Uri {
        val savedUri = preferencesManager.reminderToneUri
        return if (!savedUri.isNullOrEmpty()) {
            soundHelper.stringToUri(savedUri) ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
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
        
        // Lấy sound URI từ preferences
        val soundUri = getSoundUri()
        android.util.Log.d("NotificationHelper", "Creating reminder notification with sound URI: $soundUri")
        
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Replace with app icon
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true)
            .apply {
                // Trên Android < 8.0, set sound trong builder
                // Trên Android 8.0+, sound được set trong notification channel
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    setSound(soundUri)
                    android.util.Log.d("NotificationHelper", "Set sound in builder for Android < 8.0")
                } else {
                    // Verify channel sound
                    val channel = notificationManager.getNotificationChannel(Constants.NOTIFICATION_CHANNEL_ID)
                    android.util.Log.d("NotificationHelper", "Channel sound: ${channel?.sound}")
                }
            }
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
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
        
        // Lấy sound URI từ preferences
        val soundUri = getSoundUri()
        
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // TODO: Replace with app icon
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .apply {
                // Trên Android < 8.0, set sound trong builder
                // Trên Android 8.0+, sound được set trong notification channel
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    setSound(soundUri)
                }
            }
            .setDefaults(NotificationCompat.DEFAULT_VIBRATE or NotificationCompat.DEFAULT_LIGHTS)
            .build()
        
        notificationManager.notify(notificationId, notification)
    }
}
