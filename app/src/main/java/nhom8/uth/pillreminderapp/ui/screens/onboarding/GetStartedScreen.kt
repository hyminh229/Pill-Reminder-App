package nhom8.uth.pillreminderapp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.theme.DarkBlue
import nhom8.uth.pillreminderapp.ui.theme.LightBlue
import nhom8.uth.pillreminderapp.ui.theme.PillReminderAppTheme

/**
 * Màn hình Get Started - Giới thiệu ứng dụng
 */
@Composable
fun GetStartedScreen(
    onGetStarted: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            // Icon stopwatch với pills
            Image(
                painter = painterResource(id = R.drawable.pill_reminder_logo),
                contentDescription = "Pill Reminder Logo",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 48.dp),
                colorFilter = ColorFilter.tint(androidx.compose.ui.graphics.Color.White)
            )

            Text(
                text = "Get medication reminders",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "And much more...",
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 64.dp)
            )

            // Get Started button
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GetStartedScreenPreview() {
    PillReminderAppTheme {
        GetStartedScreen(onGetStarted = {})
    }
}
