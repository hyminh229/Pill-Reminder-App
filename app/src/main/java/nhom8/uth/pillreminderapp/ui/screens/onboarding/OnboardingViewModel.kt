package nhom8.uth.pillreminderapp.ui.screens.onboarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nhom8.uth.pillreminderapp.util.NotificationHelper
import nhom8.uth.pillreminderapp.util.PreferencesManager
import nhom8.uth.pillreminderapp.util.SoundHelper
import nhom8.uth.pillreminderapp.util.SoundItem
import javax.inject.Inject

/**
 * ViewModel quản lý state và logic cho onboarding flow
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val soundHelper: SoundHelper,
    private val notificationHelper: NotificationHelper,
    @ApplicationContext private val context: Context
) : ViewModel() {
    
    private val _isFirstLaunch = MutableStateFlow(preferencesManager.isFirstLaunch)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()
    
    /**
     * Kiểm tra xem có phải lần đầu khởi động app không
     */
    fun checkFirstLaunch(): Boolean {
        return preferencesManager.isFirstLaunch
    }
    
    /**
     * Lưu nickname của người dùng
     */
    fun saveNickname(nickname: String) {
        viewModelScope.launch {
            preferencesManager.nickname = nickname.trim()
        }
    }
    
    /**
     * Lấy danh sách sounds từ res/raw
     */
    fun getAvailableSounds(): List<SoundItem> {
        return soundHelper.getNotificationSounds()
    }
    
    /**
     * Lưu reminder tone preference (từ SoundItem)
     */
    fun saveReminderTone(soundItem: SoundItem) {
        viewModelScope.launch {
            preferencesManager.reminderTone = soundItem.title
            preferencesManager.reminderToneUri = soundHelper.uriToString(soundItem.uri)
            preferencesManager.reminderToneRawName = soundItem.rawResourceName
            // Cập nhật notification channel với sound mới
            notificationHelper.updateNotificationChannel()
        }
    }
    
    /**
     * Lưu reminder tone preference (từ String - backward compatibility)
     */
    fun saveReminderTone(tone: String) {
        viewModelScope.launch {
            preferencesManager.reminderTone = tone
        }
    }
    
    /**
     * Lấy Context (để sử dụng trong Composable nếu cần)
     */
    fun getContext(): Context {
        return context
    }
    
    /**
     * Hoàn thành onboarding - đánh dấu đã hoàn thành onboarding
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            preferencesManager.isFirstLaunch = false
            _isFirstLaunch.value = false
        }
    }
    
    /**
     * Lấy nickname hiện tại
     */
    fun getNickname(): String? {
        return preferencesManager.nickname
    }
    
    /**
     * Lấy reminder tone hiện tại
     */
    fun getReminderTone(): String {
        return preferencesManager.reminderTone
    }
    
    /**
     * Phát âm thanh để nghe thử (preview)
     */
    fun previewSound(soundItem: SoundItem) {
        soundHelper.previewSound(soundItem.uri)
    }
    
    /**
     * Dừng phát âm thanh preview
     */
    fun stopPreview() {
        soundHelper.stopPreview()
    }
}

