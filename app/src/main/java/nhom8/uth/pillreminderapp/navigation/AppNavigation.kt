package nhom8.uth.pillreminderapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import nhom8.uth.pillreminderapp.ui.screens.add_med.AddMedScreen
import nhom8.uth.pillreminderapp.ui.screens.home.HomeScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.AllDoneScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.GetStartedScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.NicknameScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.NotificationPermissionScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.ReminderToneScreen
import nhom8.uth.pillreminderapp.ui.screens.settings.SettingScreen
import nhom8.uth.pillreminderapp.ui.screens.splash.SplashScreen
import nhom8.uth.pillreminderapp.ui.screens.statistics.StatisticsScreen

/**
 * Navigation graph cho ứng dụng
 * Quản lý điều hướng giữa các màn hình
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ========== Onboarding Flow ==========
        
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToNext = {
                    // TODO: Check if first launch, navigate to GetStarted or Home
                    navController.navigate(Screen.GetStarted.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.GetStarted.route) {
            GetStartedScreen(
                onGetStarted = {
                    navController.navigate(Screen.Nickname.route)
                }
            )
        }
        
        composable(Screen.Nickname.route) {
            NicknameScreen(
                onNext = { nickname ->
                    // TODO: Save nickname
                    navController.navigate(Screen.ReminderTone.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.ReminderTone.route) {
            ReminderToneScreen(
                onNext = { tone ->
                    // TODO: Save reminder tone
                    navController.navigate(Screen.NotificationPermission.route)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.NotificationPermission.route) {
            NotificationPermissionScreen(
                onAllow = {
                    // TODO: Request notification permission
                    navController.navigate(Screen.AllDone.route)
                },
                onSkip = {
                    navController.navigate(Screen.AllDone.route)
                }
            )
        }
        
        composable(Screen.AllDone.route) {
            AllDoneScreen(
                onLetsGo = {
                    navController.navigate(Screen.Home.route) {
                        // Clear onboarding stack
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        // ========== Main App Screens ==========
        
        composable(Screen.Home.route) {
            HomeScreen(
                onAddMedClick = {
                    navController.navigate(Screen.AddMed.route)
                },
                onMedClick = { medicineId ->
                    navController.navigate(Screen.EditMed.createRoute(medicineId))
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.AddMed.route) {
            AddMedScreen(
                onSave = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            route = Screen.EditMed.ROUTE,
            arguments = listOf(
                navArgument("medicineId") {
                    type = androidx.navigation.NavType.LongType
                }
            )
        ) { backStackEntry ->
            val medicineId = backStackEntry.arguments?.getLong("medicineId") ?: -1L
            // TODO: Create EditMedScreen
            // EditMedScreen(
            //     medicineId = medicineId,
            //     onSave = { navController.popBackStack() },
            //     onCancel = { navController.popBackStack() }
            // )
            // Temporary: navigate back
            navController.popBackStack()
        }
        
        composable(Screen.Statistics.route) {
            StatisticsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToStatistics = {
                    navController.navigate(Screen.Statistics.route)
                }
            )
        }
    }
}
