package nhom8.uth.pillreminderapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.components.ReminderCard

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
    val completedMedicines by viewModel.completedMedicines.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Pull to refresh state
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    Scaffold(
        topBar = {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Logo icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_pill),
                    contentDescription = "Pills Reminder",
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF5FB9E6) // Màu xanh cho logo
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Text "Pills Reminder"
                Text(
                    text = "Pills Reminder",
                    style = MaterialTheme.typography.titleLarge,
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
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Color.Black,
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Medicine")
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Current Screen */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color(0xFF5FB9E6),
                        selectedTextColor = Color(0xFF5FB9E6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToStatistics,
                    icon = {
                        Icon(
                            Icons.AutoMirrored.Filled.List,
                            contentDescription = "Progress"
                        )
                    },
                    label = { Text("Progress") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color(0xFF5FB9E6),
                        selectedTextColor = Color(0xFF5FB9E6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSettings,
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Setting") },
                    label = { Text("Setting") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color(0xFF5FB9E6),
                        selectedTextColor = Color(0xFF5FB9E6),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.loadMedicines() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isLoading && overdueMedicines.isEmpty() && todaySchedule.isEmpty() && completedMedicines.isEmpty()) {
                    // Chỉ hiển thị loading indicator khi chưa có dữ liệu
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                    // Overdue Section with blue border
                    if (overdueMedicines.isNotEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF5FB9E6), // Light blue border
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .background(
                                        color = Color.White,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Overdue",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    overdueMedicines.forEach { reminder ->
                                        ReminderCard(
                                            medicineName = reminder.medicine.name,
                                            reminderTime = reminder.reminderTime,
                                            status = reminder.status,
                                            intakeAdvice = reminder.medicine.intakeAdvice,
                                            onCardClick = {
                                                onMedClick(reminder.medicine.id)
                                            },
                                            onCheckboxClick = {
                                                if (reminder.status != ReminderStatus.COMPLETED) {
                                                    viewModel.markAsTaken(reminder)
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Today's Schedule Section
                    item {
                        Text(
                            text = "Today's Schedule",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(
                                top = if (overdueMedicines.isNotEmpty()) 8.dp else 0.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                    if (todaySchedule.isEmpty() && overdueMedicines.isEmpty() && completedMedicines.isEmpty()) {
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
                                        painter = painterResource(id = R.drawable.logo),
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
                                    viewModel.markAsTaken(reminder)
                                }
                            )
                        }
                    }

                    // Completed Section
                    if (completedMedicines.isNotEmpty()) {
                        item {
                            Text(
                                text = "Completed",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(
                                    top = if (todaySchedule.isNotEmpty() || overdueMedicines.isNotEmpty()) 8.dp else 0.dp,
                                    bottom = 8.dp
                                )
                            )
                        }

                        items(completedMedicines) { reminder ->
                            ReminderCard(
                                medicineName = reminder.medicine.name,
                                reminderTime = reminder.reminderTime,
                                status = reminder.status,
                                intakeAdvice = reminder.medicine.intakeAdvice,
                                onCardClick = {
                                    onMedClick(reminder.medicine.id)
                                },
                                onCheckboxClick = { },
                                modifier = Modifier.alpha(0.5f)
                            )
                        }
                    }
                }
                }
            }
        }
    }
}