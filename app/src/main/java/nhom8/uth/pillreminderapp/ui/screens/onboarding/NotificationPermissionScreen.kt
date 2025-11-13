package nhom8.uth.pillreminderapp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.theme.DarkBlue
import nhom8.uth.pillreminderapp.ui.theme.LightBlue

/**
 * Màn hình yêu cầu Notification Permission - Xin quyền hiển thị thông báo
 */
@Composable
fun NotificationPermissionScreen(
    onAllow: () -> Unit,
    onSkip: () -> Unit
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
            // Icon
            Image(
                painter = painterResource(id = R.drawable.pill_reminder_logo),
                contentDescription = "Pill Reminder Logo",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 48.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )

            Text(
                text = "Never miss a dose!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Let's make sure you get reminders exactly when you need them.",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 64.dp)
            )

            // Allow notifications button
            Button(
                onClick = onAllow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue
                )
            ) {
                Text(
                    text = "Allow notifications",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // No thanks link
            TextButton(
                onClick = onSkip,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No thanks!",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}
