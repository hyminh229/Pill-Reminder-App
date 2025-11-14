package nhom8.uth.pillreminderapp.util

/**
 * Constants cho ứng dụng Pill Reminder
 */
object Constants {
    // Notification
    const val NOTIFICATION_CHANNEL_ID = "pill_reminder_channel"
    const val NOTIFICATION_CHANNEL_NAME = "Pill Reminders"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "Notifications for medication reminders"
    
    // WorkManager
    const val REMINDER_WORK_TAG = "reminder_work"
    const val REMINDER_WORK_NAME_PREFIX = "reminder_"
    
    // Database
    const val DATABASE_NAME = "pill_reminder_database"
    
    // SharedPreferences
    const val PREFS_NAME = "pill_reminder_prefs"
    const val KEY_FIRST_LAUNCH = "first_launch"
    const val KEY_NICKNAME = "nickname"
    const val KEY_REMINDER_TONE = "reminder_tone"
    const val KEY_THEME = "theme"
    
    // Medicine Units
    val MEDICINE_UNITS = listOf(
        "pills",
        "ampoule(s)",
        "application(s)",
        "drop(s)",
        "gram(s)",
        "Injection(s)",
        "miligram(s)",
        "teaspoon(s)"
    )
    
    // Intake Advice Options
    val INTAKE_ADVICE_OPTIONS = listOf(
        "None",
        "Before meal",
        "With meal",
        "After meal"
    )
    
    // Repeat Options
    val REPEAT_OPTIONS = listOf(
        "Daily",
        "Weekly",
        "Custom"
    )
}

