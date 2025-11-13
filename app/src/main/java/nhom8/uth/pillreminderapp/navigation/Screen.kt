package nhom8.uth.pillreminderapp.navigation

import nhom8.uth.pillreminderapp.data.database.entity.MedicineEntity

/**
 * Sealed class định nghĩa tất cả các màn hình trong ứng dụng
 * Sử dụng cho Navigation Compose
 */
sealed class Screen(val route: String) {
    
    // ========== Onboarding Flow ==========
    
    /**
     * Màn hình Splash - Hiển thị khi khởi động app
     */
    object Splash : Screen("splash")
    
    /**
     * Màn hình Get Started - Giới thiệu ứng dụng
     */
    object GetStarted : Screen("get_started")
    
    /**
     * Màn hình nhập Nickname
     */
    object Nickname : Screen("nickname")
    
    /**
     * Màn hình chọn Reminder Tone
     */
    object ReminderTone : Screen("reminder_tone")
    
    /**
     * Màn hình yêu cầu Notification Permission
     */
    object NotificationPermission : Screen("notification_permission")
    
    /**
     * Màn hình All Done - Hoàn thành onboarding
     */
    object AllDone : Screen("all_done")
    
    // ========== Main App Screens ==========
    
    /**
     * Màn hình Home - Danh sách thuốc hôm nay
     */
    object Home : Screen("home")
    
    /**
     * Màn hình thêm thuốc mới
     */
    object AddMed : Screen("add_med")
    
    /**
     * Màn hình sửa thuốc
     * @param medicineId ID của thuốc cần sửa
     */
    data class EditMed(val medicineId: Long = -1L) : Screen("edit_med/{medicineId}") {
        companion object {
            const val ROUTE = "edit_med/{medicineId}"
            
            fun createRoute(medicineId: Long) = "edit_med/$medicineId"
        }
    }
    
    /**
     * Màn hình Statistics/Progress - Thống kê và biểu đồ
     */
    object Statistics : Screen("statistics")
    
    /**
     * Màn hình Settings - Cài đặt
     */
    object Settings : Screen("settings")
}

/**
 * Helper functions để tạo routes với parameters
 */
object NavigationRoutes {
    const val SPLASH = "splash"
    const val GET_STARTED = "get_started"
    const val NICKNAME = "nickname"
    const val REMINDER_TONE = "reminder_tone"
    const val NOTIFICATION_PERMISSION = "notification_permission"
    const val ALL_DONE = "all_done"
    const val HOME = "home"
    const val ADD_MED = "add_med"
    const val EDIT_MED = "edit_med/{medicineId}"
    const val STATISTICS = "statistics"
    const val SETTINGS = "settings"
    
    /**
     * Tạo route cho EditMed với medicineId
     */
    fun editMed(medicineId: Long): String {
        return "edit_med/$medicineId"
    }
}
