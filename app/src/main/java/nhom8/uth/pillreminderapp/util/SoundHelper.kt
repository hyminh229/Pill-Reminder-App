package nhom8.uth.pillreminderapp.util

import android.content.Context
import android.content.res.Resources
import android.media.Ringtone
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
    val isDefault: Boolean = false,
    val rawResourceName: String? = null // Tên file trong res/raw (không có extension)
)

/**
 * Helper class để lấy danh sách notification sounds từ res/raw
 */
@Singleton
class SoundHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private var currentRingtone: Ringtone? = null
    
    /**
     * Lấy danh sách notification sounds từ res/raw
     */
    fun getNotificationSounds(): List<SoundItem> {
        val sounds = mutableListOf<SoundItem>()
        
        try {
            val resources: Resources = context.resources
            val packageName: String = context.packageName
            
            // Danh sách các file âm thanh trong res/raw với tên hiển thị
            val rawSounds = mapOf(
                "arabian_mystery_harp_notification" to "Arabian Mystery",
                "bell_notification" to "Bell",
                "clear_announce_tones" to "Clear Announce",
                "correct_answer_reward" to "Correct Answer",
                "correct_answer_tone" to "Correct Tone",
                "doorbell_single_press" to "Doorbell",
                "happy_bells_notification" to "Happy Bells",
                "positive_notification" to "Positive",
                "sci_fi_click" to "Sci-Fi Click",
                "software_interface_back" to "Interface Back",
                "software_interface_remove" to "Interface Remove",
                "software_interface_start" to "Interface Start",
                "wrong_answer_fail_notification" to "Wrong Answer"
            )
            
            // Tạo URI cho mỗi file trong res/raw
            rawSounds.forEach { (rawName, displayName) ->
                val resourceId = resources.getIdentifier(rawName, "raw", packageName)
                if (resourceId != 0) {
                    val uri = Uri.parse("android.resource://$packageName/$resourceId")
                    sounds.add(SoundItem(
                        title = displayName,
                        uri = uri,
                        isDefault = false,
                        rawResourceName = rawName
                    ))
                }
            }
            
            // Sắp xếp theo tên
            sounds.sortBy { it.title }
            
            // Thêm default sound ở đầu danh sách
            val defaultSound = SoundItem(
                title = "Default",
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                isDefault = true,
                rawResourceName = null
            )
            sounds.add(0, defaultSound)
            
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback: thêm default sound
            sounds.add(SoundItem(
                title = "Default",
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                isDefault = true,
                rawResourceName = null
            ))
        }
        
        return sounds.ifEmpty {
            // Nếu không có sound nào, trả về default
            listOf(SoundItem(
                title = "Default",
                uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                isDefault = true,
                rawResourceName = null
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
    
    /**
     * Phát âm thanh để nghe thử (preview)
     */
    fun previewSound(uri: Uri) {
        try {
            // Dừng âm thanh hiện tại nếu đang phát
            stopPreview()
            
            // Phát âm thanh mới
            val ringtone = RingtoneManager.getRingtone(context, uri)
            if (ringtone != null) {
                currentRingtone = ringtone
                ringtone.play()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Dừng phát âm thanh preview
     */
    fun stopPreview() {
        try {
            currentRingtone?.stop()
            currentRingtone = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

