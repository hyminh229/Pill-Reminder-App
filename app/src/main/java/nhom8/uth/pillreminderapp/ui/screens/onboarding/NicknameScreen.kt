package nhom8.uth.pillreminderapp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.theme.LightBlue

/**
 * Màn hình nhập Nickname - Yêu cầu người dùng nhập tên
 */
@Composable
fun NicknameScreen(
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    var nickname by remember { mutableStateOf("") }

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
                text = "To start with, what should we call you?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // TextField với clear button
            OutlinedTextField(
                value = nickname,
                onValueChange = { nickname = it },
                placeholder = {
                    Text(
                        text = "Nickname",
                        color = Color.Gray
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                trailingIcon = {
                    if (nickname.isNotEmpty()) {
                        IconButton(onClick = { nickname = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = Color.White
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (nickname.isNotBlank()) {
                            onNext(nickname)
                        }
                    }
                ),
                singleLine = true
            )

            // Next button
            Button(
                onClick = { onNext(nickname) },
                enabled = nickname.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = nhom8.uth.pillreminderapp.ui.theme.DarkBlue,
                    disabledContainerColor = Color.Gray
                )
            ) {
                Text(
                    text = "Next",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
