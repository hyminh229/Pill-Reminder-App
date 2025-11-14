package nhom8.uth.pillreminderapp.util

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Data class cho sound item
 */
data class SoundItem(
    val title: String,
    val uri: Uri,
    val isDefault: Boolean = false
)

/**
 * Helper class để lấy danh sách notification sounds từ hệ thống
 */
@Singleton
class SoundHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    /**
     * Lấy danh sách notification sounds có sẵn trong máy
     */
    fun getNotificationSounds(): List<SoundItem> {
        val sounds = mutableListOf<SoundItem>()
        
        try {
            val manager = RingtoneManager(context)
            manager.setType(RingtoneManager.TYPE_NOTIFICATION)
            val cursor = manager.cursor
            
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
                    val uri = manager.getRingtoneUri(cursor.position)
                    val isDefault = uri == RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    
                    sounds.add(SoundItem(
                        title = title,
                        uri = uri,
                        isDefault = isDefault
                    ))
                } while (cursor.moveToNext())
            }
            
            cursor?.close()
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: thêm default sound
            sounds.add(SoundItem(
                title = "Default",
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                isDefault = true
            ))
        }
        
        return sounds.ifEmpty {
            // Nếu không có sound nào, trả về default
            listOf(SoundItem(
                title = "Default",
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                isDefault = true
            ))
        }
    }
    
    /**
     * Lấy tên của sound từ URI
     */
    fun getSoundTitle(uri: Uri?): String {
        if (uri == null) return "Default"
        
        return try {
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.getTitle(context) ?: "Default"
        } catch (e: Exception) {
            "Default"
        }
    }
    
    /**
     * Chuyển URI thành String để lưu vào Preferences
     */
    fun uriToString(uri: Uri?): String {
        return uri?.toString() ?: ""
    }
    
    /**
     * Chuyển String thành URI từ Preferences
     */
    fun stringToUri(uriString: String?): Uri? {
        return if (uriString.isNullOrEmpty()) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else {
            try {
                Uri.parse(uriString)
            } catch (e: Exception) {
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
        }
    }
}

