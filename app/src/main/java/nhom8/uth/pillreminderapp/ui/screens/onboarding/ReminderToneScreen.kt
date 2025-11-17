package nhom8.uth.pillreminderapp.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import nhom8.uth.pillreminderapp.ui.theme.OnboardingBlue
import nhom8.uth.pillreminderapp.ui.theme.PillReminderAppTheme
import nhom8.uth.pillreminderapp.util.SoundItem

@Composable
fun ReminderToneScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNext: (String) -> Unit
) {
    var selectedSoundItem by remember { mutableStateOf<SoundItem?>(null) }
    var expanded by remember { mutableStateOf(false) }
    var sounds by remember { mutableStateOf<List<SoundItem>>(emptyList()) }
    
    // Load sounds from res/raw
    LaunchedEffect(Unit) {
        sounds = viewModel.getAvailableSounds()
        if (sounds.isNotEmpty()) {
            selectedSoundItem = sounds.first()
        }
    }
    
    // Dừng preview khi rời khỏi màn hình
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopPreview()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OnboardingBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Pick your reminder tone",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "For what matters most, choose a sound you won't ignore",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // Sound selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { expanded = true }
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Sound", color = Color.White, fontSize = 16.sp)
                        Text(
                            text = selectedSoundItem?.title ?: "Default",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        sounds.forEach { sound ->
                            DropdownMenuItem(
                                text = { Text(sound.title) },
                                onClick = {
                                    selectedSoundItem = sound
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                // Preview button
                IconButton(
                    onClick = {
                        selectedSoundItem?.let { viewModel.previewSound(it) }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Preview sound",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "You can always change this later",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    selectedSoundItem?.let { soundItem ->
                        viewModel.saveReminderTone(soundItem)
                        onNext(soundItem.title)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00838F) // Darker teal
                ),
                enabled = selectedSoundItem != null
            ) {
                Text(
                    text = "Next",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderToneScreenPreview() {
    PillReminderAppTheme {
        ReminderToneScreen(onNext = {})
    }
}
