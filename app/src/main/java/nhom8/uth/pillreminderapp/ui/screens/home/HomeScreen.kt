package nhom8.uth.pillreminderapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.components.ReminderCard
import nhom8.uth.pillreminderapp.ui.theme.DarkBlue
import nhom8.uth.pillreminderapp.ui.theme.LightBlue

/**
 * Màn hình Home - Hiển thị danh sách thuốc hôm nay và quá hạn
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onAddMedClick: () -> Unit,
    onMedClick: (Long) -> Unit,
    onNavigateToStatistics: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val overdueMedicines by viewModel.overdueMedicines.collectAsState()
    val todaySchedule by viewModel.todaySchedule.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.pill_reminder_logo),
                            contentDescription = "Pills",
                            modifier = Modifier.size(32.dp),
                            tint = LightBlue
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Pills Reminder",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddMedClick,
                containerColor = DarkBlue
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Medicine",
                    tint = Color.White
                )
            }
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
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LightBlue,
                        selectedTextColor = LightBlue,
                        indicatorColor = Color.Transparent
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = "Progress"
                        )
                    },
                    label = { Text("Progress") },
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
                    selected = false,
                    onClick = onNavigateToSettings,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = LightBlue,
                        selectedTextColor = LightBlue
                    )
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Overdue Section
                if (overdueMedicines.isNotEmpty()) {
                    item {
                        Text(
                            text = "Overdue",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    
                    items(overdueMedicines) { reminder ->
                        ReminderCard(
                            medicineName = reminder.medicine.name,
                            reminderTime = reminder.reminderTime,
                            status = reminder.status,
                            intakeAdvice = reminder.medicine.intakeAdvice,
                            onCardClick = {
                                onMedClick(reminder.medicine.id)
                            },
                            onCheckboxClick = {
                                if (reminder.status == ReminderStatus.COMPLETED) {
                                    // Already completed, do nothing or allow uncheck
                                } else {
                                    viewModel.markAsTaken(reminder)
                                }
                            }
                        )
                    }
                }
                
                // Today's Schedule Section
                item {
                    Text(
                        text = "Today's Schedule",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = if (overdueMedicines.isNotEmpty()) 8.dp else 0.dp, bottom = 8.dp)
                    )
                }
                
                if (todaySchedule.isEmpty() && overdueMedicines.isEmpty()) {
                    // Empty state
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.pill_reminder_logo),
                                    contentDescription = "Empty",
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "No medicines scheduled",
                                    fontSize = 16.sp,
                                    color = Color.Gray
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap the + button to add a medicine",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                } else {
                    items(todaySchedule) { reminder ->
                        ReminderCard(
                            medicineName = reminder.medicine.name,
                            reminderTime = reminder.reminderTime,
                            status = reminder.status,
                            intakeAdvice = reminder.medicine.intakeAdvice,
                            onCardClick = {
                                onMedClick(reminder.medicine.id)
                            },
                            onCheckboxClick = {
                                when (reminder.status) {
                                    ReminderStatus.COMPLETED -> {
                                        // Already completed, do nothing
                                    }
                                    ReminderStatus.SKIPPED -> {
                                        // Allow to mark as taken
                                        viewModel.markAsTaken(reminder)
                                    }
                                    else -> {
                                        viewModel.markAsTaken(reminder)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
