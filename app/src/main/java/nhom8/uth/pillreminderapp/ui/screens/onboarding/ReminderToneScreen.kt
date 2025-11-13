package nhom8.uth.pillreminderapp.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.theme.LightBlue

/**
 * Màn hình chọn Reminder Tone - Cho phép người dùng chọn âm thanh nhắc nhở
 */
@Composable
fun ReminderToneScreen(
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    var selectedTone by remember { mutableStateOf("Meow meow") }
    var expanded by remember { mutableStateOf(false) }
    
    val tones = listOf("Meow meow", "Default", "Chime", "Bell", "Alert")

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
                text = "Pick your reminder tone",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "For what matters most, choose a sound you won't ignore",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Sound selector
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                OutlinedTextField(
                    value = selectedTone,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = Color.White
                        )
                    },
                    label = {
                        Text(
                            text = "Sound",
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tones.forEach { tone ->
                        DropdownMenuItem(
                            text = { Text(tone) },
                            onClick = {
                                selectedTone = tone
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "You can always change this later",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Next button
            Button(
                onClick = { onNext(selectedTone) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = nhom8.uth.pillreminderapp.ui.theme.DarkBlue
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
