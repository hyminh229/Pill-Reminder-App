package nhom8.uth.pillreminderapp.ui.screens.home

import androidx.compose.runtime.Composable

/**
 * Màn hình Home - Hiển thị danh sách thuốc hôm nay và quá hạn
 */
@Composable
fun HomeScreen(
    onAddMedClick: () -> Unit,
    onMedClick: (Long) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    // TODO: Implement HomeScreen UI
    // - Top app bar với logo pills và title "Pills Reminder"
    // - Section "Overdue" với danh sách thuốc quá hạn
    // - Section "Today's Schedule" với danh sách thuốc hôm nay
    // - ReminderCard component cho mỗi thuốc
    // - FAB button (+) để thêm thuốc mới
    // - Bottom navigation bar (Home, Progress, Setting)
}

