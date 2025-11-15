package nhom8.uth.pillreminderapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import nhom8.uth.pillreminderapp.ui.theme.LightBlue
import nhom8.uth.pillreminderapp.util.SoundItem

/**
 * Màn hình Settings - Cài đặt ứng dụng
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToStatistics: () -> Unit
) {
    val reminderTone by viewModel.reminderTone.collectAsState()
    val theme by viewModel.theme.collectAsState()
    val reminderMode by viewModel.reminderMode.collectAsState()
    val availableSounds by viewModel.availableSounds.collectAsState()
    
    var showSoundPicker by remember { mutableStateOf(false) }
    var showInDevelopmentMessage by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    selected = false,
                    onClick = onNavigateToHome,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LightBlue,
                        selectedTextColor = LightBlue
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Stats"
                        )
                    },
                    label = { Text("Stats") },
                    selected = false,
                    onClick = onNavigateToStatistics,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LightBlue,
                        selectedTextColor = LightBlue
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Setting"
                        )
                    },
                    label = { Text("Setting") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LightBlue,
                        selectedTextColor = LightBlue,
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            // Header với gear icon và "Setting" text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Setting",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            // Reminder Settings Section
            Text(
                text = "Reminder Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Reminder Sound
            SettingItem(
                title = "Reminder Sound",
                value = reminderTone,
                onClick = {
                    showSoundPicker = true
                }
            )
            
            // Sound Picker Dialog
            if (showSoundPicker) {
                SoundPickerDialog(
                    sounds = availableSounds,
                    currentSound = reminderTone,
                    onSoundSelected = { soundItem ->
                        viewModel.updateReminderTone(soundItem)
                        showSoundPicker = false
                    },
                    onDismiss = {
                        showSoundPicker = false
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Reminder Mode
            SettingItem(
                title = "Reminder Mode",
                value = reminderMode,
                onClick = {
                    // Hiển thị thông báo đang phát triển
                    showInDevelopmentMessage = true
                }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // General Section
            Text(
                text = "General",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Remove ADS
            SettingItem(
                title = "Remove ADS",
                value = null,
                onClick = {
                    // Hiển thị thông báo đang phát triển
                    showInDevelopmentMessage = true
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Light or Dark Theme
            SettingItem(
                title = "Light or Dark Theme",
                value = theme,
                onClick = {
                    // Hiển thị thông báo đang phát triển
                    showInDevelopmentMessage = true
                }
            )
        }
    }
    
    // Hiển thị Snackbar khi có thông báo đang phát triển
    LaunchedEffect(showInDevelopmentMessage) {
        if (showInDevelopmentMessage) {
            snackbarHostState.showSnackbar(
                message = "Tính năng này đang được phát triển",
                duration = SnackbarDuration.Short
            )
            showInDevelopmentMessage = false
        }
    }
}

/**
 * Setting Item Component
 */
@Composable
private fun SettingItem(
    title: String,
    value: String?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
        
        if (value != null) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = LightBlue
            )
        }
    }
}

/**
 * Sound Picker Dialog
 */
@Composable
private fun SoundPickerDialog(
    sounds: List<SoundItem>,
    currentSound: String,
    onSoundSelected: (SoundItem) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Sound",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                }
                
                HorizontalDivider()
                
                // Sound List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(sounds) { sound ->
                        SoundItemRow(
                            sound = sound,
                            isSelected = sound.title == currentSound,
                            onClick = {
                                onSoundSelected(sound)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Sound Item Row trong dialog
 */
@Composable
private fun SoundItemRow(
    sound: SoundItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sound.title,
                fontSize = 16.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) LightBlue else Color.Black
            )
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Selected",
                    tint = LightBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color(0xFFE0E0E0)
        )
    }
}

