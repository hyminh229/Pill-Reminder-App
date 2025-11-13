package nhom8.uth.pillreminderapp.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.theme.LightBlue
import nhom8.uth.pillreminderapp.ui.theme.MediumBlue
import kotlinx.coroutines.delay

/**
 * Màn hình Splash - Hiển thị logo và loading khi khởi động app
 */
@Composable
fun SplashScreen(
    onNavigateToNext: () -> Unit
) {
    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_alpha"
    )

    LaunchedEffect(Unit) {
        delay(2500) // Auto navigate sau 2.5 giây
        onNavigateToNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MediumBlue,
                        LightBlue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Logo với stopwatch và pills
            Image(
                painter = painterResource(id = R.drawable.pill_reminder_logo),
                contentDescription = "Pill Reminder Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 24.dp),
                colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.White)
            )

            Text(
                text = "Pill Remider",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "Stay healthy and track your daily medicines intake",
                fontSize = 16.sp,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Loading indicator
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = androidx.compose.ui.graphics.Color.White,
                strokeWidth = 3.dp
            )
        }
    }
}
