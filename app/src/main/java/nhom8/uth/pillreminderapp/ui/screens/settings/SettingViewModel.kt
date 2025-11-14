package nhom8.uth.pillreminderapp.ui.screens.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nhom8.uth.pillreminderapp.util.PreferencesManager
import nhom8.uth.pillreminderapp.util.SoundHelper
import nhom8.uth.pillreminderapp.util.SoundItem
import javax.inject.Inject

/**
 * ViewModel quản lý logic cho màn hình Settings
 */
@HiltViewModel
class SettingViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val soundHelper: SoundHelper
) : ViewModel() {
    
    // StateFlow cho reminder tone
    private val _reminderTone = MutableStateFlow(preferencesManager.reminderTone)
    val reminderTone: StateFlow<String> = _reminderTone.asStateFlow()
    
    // StateFlow cho theme
    private val _theme = MutableStateFlow(preferencesManager.theme)
    val theme: StateFlow<String> = _theme.asStateFlow()
    
    // StateFlow cho danh sách sounds
    private val _availableSounds = MutableStateFlow<List<SoundItem>>(emptyList())
    val availableSounds: StateFlow<List<SoundItem>> = _availableSounds.asStateFlow()
    
    // Reminder mode (mặc định "As device settings")
    val reminderMode = MutableStateFlow("As device settings")
    
    init {
        loadSettings()
        loadAvailableSounds()
    }
    
    /**
     * Load danh sách sounds có sẵn
     */
    private fun loadAvailableSounds() {
        viewModelScope.launch {
            _availableSounds.value = soundHelper.getNotificationSounds()
        }
    }
    
    /**
     * Update reminder tone
     */
    fun updateReminderTone(soundItem: SoundItem) {
        viewModelScope.launch {
            preferencesManager.reminderTone = soundItem.title
            preferencesManager.reminderToneUri = soundHelper.uriToString(soundItem.uri)
            _reminderTone.value = soundItem.title
        }
    }
    
    /**
     * Update theme
     */
    fun updateTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.theme = theme
            _theme.value = theme
        }
    }
    
    /**
     * Load settings từ PreferencesManager
     */
    fun loadSettings() {
        // Load reminder tone từ URI nếu có, nếu không thì dùng tên
        val savedUri = preferencesManager.reminderToneUri
        if (!savedUri.isNullOrEmpty()) {
            val uri = soundHelper.stringToUri(savedUri)
            _reminderTone.value = soundHelper.getSoundTitle(uri)
        } else {
            _reminderTone.value = preferencesManager.reminderTone
        }
        _theme.value = preferencesManager.theme
    }
}

