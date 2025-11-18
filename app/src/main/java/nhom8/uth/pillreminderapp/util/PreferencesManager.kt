package nhom8.uth.pillreminderapp.util

import android.content.Context
import android.content.SharedPreferences

/**
 * Quản lý SharedPreferences/DataStore cho các preferences của ứng dụng
 * - First launch flag
 * - User nickname
 * - Reminder tone preference
 * - Theme preference
 */
class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "pill_reminder_prefs",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_NICKNAME = "nickname"
        private const val KEY_REMINDER_TONE = "reminder_tone"
        private const val KEY_REMINDER_TONE_URI = "reminder_tone_uri"
        private const val KEY_REMINDER_TONE_RAW_NAME = "reminder_tone_raw_name"
        private const val KEY_THEME = "theme"
    }

    var isFirstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    var nickname: String?
        get() = prefs.getString(KEY_NICKNAME, null)
        set(value) = prefs.edit().putString(KEY_NICKNAME, value).apply()

    var reminderTone: String
        get() = prefs.getString(KEY_REMINDER_TONE, "Meow meow") ?: "Meow meow"
        set(value) = prefs.edit().putString(KEY_REMINDER_TONE, value).apply()
    
    // Lưu reminder tone URI (để sử dụng với RingtoneManager)
    var reminderToneUri: String?
        get() = prefs.getString(KEY_REMINDER_TONE_URI, null)
        set(value) = prefs.edit().putString(KEY_REMINDER_TONE_URI, value).apply()
    
    // Lưu raw resource name để có thể convert sang file URI cho notification channel
    var reminderToneRawName: String?
        get() = prefs.getString(KEY_REMINDER_TONE_RAW_NAME, null)
        set(value) = prefs.edit().putString(KEY_REMINDER_TONE_RAW_NAME, value).apply()

    var theme: String
        get() = prefs.getString(KEY_THEME, "Light") ?: "Light"
        set(value) = prefs.edit().putString(KEY_THEME, value).apply()
}

