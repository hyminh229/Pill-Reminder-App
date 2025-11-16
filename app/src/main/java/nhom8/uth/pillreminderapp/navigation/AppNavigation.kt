package nhom8.uth.pillreminderapp.navigation

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import nhom8.uth.pillreminderapp.ui.screens.add_med.AddMedScreen
import nhom8.uth.pillreminderapp.ui.screens.home.HomeScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.AllDoneScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.GetStartedScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.NicknameScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.NotificationPermissionScreen
import nhom8.uth.pillreminderapp.ui.screens.onboarding.OnboardingViewModel
import nhom8.uth.pillreminderapp.ui.screens.onboarding.ReminderToneScreen
import nhom8.uth.pillreminderapp.ui.screens.settings.SettingScreen
import nhom8.uth.pillreminderapp.ui.screens.splash.SplashScreen
import nhom8.uth.pillreminderapp.ui.screens.statistics.StatisticsScreen

/**
 * Navigation graph cho ứng dụng
 * Quản lý điều hướng giữa các màn hình
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    modifier: Modifier = Modifier
) {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val isFirstLaunch by onboardingViewModel.isFirstLaunch.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ========== Onboarding Flow ==========

        composable(Screen.Splash.route) {
            SplashScreen()
            LaunchedEffect(Unit) {
                delay(2500)
                // Check if first launch, navigate to GetStarted or Home
                if (onboardingViewModel.checkFirstLaunch()) {
                    navController.navigate(Screen.GetStarted.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        composable(Screen.GetStarted.route) {
            GetStartedScreen(
                onNavigate = {
                    navController.navigate(Screen.Nickname.route)
                }
            )
        }

        composable(Screen.Nickname.route) {
            NicknameScreen(
                onNext = { nickname ->
                    // Save nickname
                    onboardingViewModel.saveNickname(nickname)
                    navController.navigate(Screen.ReminderTone.route)
                }
            )
        }

        composable(Screen.ReminderTone.route) {
            ReminderToneScreen(
                onNext = { tone ->
                    // Save reminder tone
                    onboardingViewModel.saveReminderTone(tone)
                    navController.navigate(Screen.NotificationPermission.route)
                }
            )
        }

        composable(Screen.NotificationPermission.route) {
            // Request notification permission (Android 13+)
            val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                rememberMultiplePermissionsState(
                    permissions = listOf(Manifest.permission.POST_NOTIFICATIONS)
                )
            } else {
                null
            }

            NotificationPermissionScreen(
                onAllow = {
                    // Request notification permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && notificationPermission != null) {
                        notificationPermission.launchMultiplePermissionRequest()
                    }
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
                    // Complete onboarding
                    onboardingViewModel.completeOnboarding()
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
            AddMedScreen(
                medicineId = medicineId,
                onSave = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
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
