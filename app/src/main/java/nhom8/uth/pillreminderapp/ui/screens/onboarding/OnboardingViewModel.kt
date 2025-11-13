package nhom8.uth.pillreminderapp.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nhom8.uth.pillreminderapp.util.PreferencesManager
import javax.inject.Inject

/**
 * ViewModel quản lý state và logic cho onboarding flow
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
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
     * Lưu reminder tone preference
     */
    fun saveReminderTone(tone: String) {
        viewModelScope.launch {
            preferencesManager.reminderTone = tone
        }
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
}

