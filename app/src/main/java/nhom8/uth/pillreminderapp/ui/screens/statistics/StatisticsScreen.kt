package nhom8.uth.pillreminderapp.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.data.database.entity.HistoryEntity
import nhom8.uth.pillreminderapp.ui.components.ChartSegment
import nhom8.uth.pillreminderapp.ui.components.StatPieChart
import nhom8.uth.pillreminderapp.ui.theme.LightBlue
import java.text.SimpleDateFormat
import java.util.*

/**
 * Màn hình Statistics - Hiển thị thống kê và biểu đồ
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val chartData by viewModel.chartData.collectAsState()
    val totalValue by viewModel.totalValue.collectAsState()
    val historyList by viewModel.historyList.collectAsState()
    val timeFilter by viewModel.timeFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    var selectedTab by remember { mutableStateOf(0) } // 0 = Charts, 1 = List
    
    Scaffold(

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
                            contentDescription = "Progress"
                        )
                    },
                    label = { Text("Progress") },
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
    ) { paddingValues -> // Đây là nội dung chính của màn hình

        // --- COLUMN CHÍNH CHỨA TOÀN BỘ NỘI DUNG ---
        // (Đây chính là Column bạn đã đặt sai chỗ)
        Column(
            modifier = Modifier
                .fillMaxSize()
                // 1. Chỉ lấy padding DƯỚI từ Scaffold (để né BottomBar)
                .padding(bottom = paddingValues.calculateBottomPadding())
                // 2. Tự né thanh trạng thái (status bar)
                .statusBarsPadding()
                // 3. Padding ngang cho toàn bộ nội dung
                .padding(horizontal = 16.dp)
        ) {
            // Tiêu đề Progress
            Text(
                text = "Progress",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp) // Thêm chút padding trên
            )

            // Hàng chứa 2 Tab
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp) // Thêm khoảng cách với tiêu đề
            ) {
                // 1. Tab CHARTS (Bên trái - chiếm 50%)
                Column(
                    modifier = Modifier
                        .weight(1f) // Chiếm 50%
                        .clickable { selectedTab = 0 },
                    horizontalAlignment = Alignment.CenterHorizontally // Căn giữa chữ
                ) {
                    Text(
                        text = "Charts",
                        fontSize = 18.sp,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == 0) Color.Black else Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    // Thanh gạch dưới (Indicator)
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 3.dp,
                        color = if (selectedTab == 0) Color.Black else Color.LightGray.copy(alpha = 0.3f)
                    )
                }

                // 2. Tab LIST (Bên phải - chiếm 50%)
                Column(
                    modifier = Modifier
                        .weight(1f) // Chiếm 50%
                        .clickable { selectedTab = 1 },
                    horizontalAlignment = Alignment.CenterHorizontally // Căn giữa chữ
                ) {
                    Text(
                        text = "List",
                        fontSize = 18.sp,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                        color = if (selectedTab == 1) Color.Black else Color.Gray,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    // Thanh gạch dưới
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 3.dp,
                        color = if (selectedTab == 1) Color.Black else Color.LightGray.copy(alpha = 0.3f)
                    )
                }
            } // Hết Row chứa Tab

            // --- NỘI DUNG CHÍNH (BIỂU ĐỒ HOẶC DANH SÁCH) ---
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Lấp đầy phần còn lại
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                when (selectedTab) {
                    0 -> ChartsTab(
                        chartData = chartData,
                        totalValue = totalValue,
                        // Thêm modifier này để nó lấp đầy không gian còn lại
                        modifier = Modifier.fillMaxSize()
                    )
                    1 -> ListTab(
                        historyList = historyList,
                        // Thêm modifier này để nó lấp đầy không gian còn lại
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * Charts Tab - Hiển thị donut chart và data table
 */
@Composable
private fun ChartsTab(
    chartData: List<ChartItem>,
    totalValue: Int,
    modifier: Modifier = Modifier
) {
    if (chartData.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "No data available",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Add medicines and mark them as taken to see statistics",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item {
                // Donut Chart - căn giữa
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    StatPieChart(
                        segments = chartData.map { 
                            ChartSegment(
                                value = it.value.toFloat(),
                                color = it.color
                            )
                        },
                        totalValue = totalValue
                    )
                }
            }
            
            item {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp
                )
            }
            
            // Data Table
            item {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Label",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF757575),
                            modifier = Modifier.weight(2f)
                        )
                        Text(
                            text = "Value",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF757575),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = "%",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF757575),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                    }
                    
                    // Data rows
                    chartData.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Label với color dot
                            Row(
                                modifier = Modifier.weight(2f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(item.color, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = item.medicineName,
                                    fontSize = 15.sp,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                            
                            // Value
                            Text(
                                text = item.value.toString(),
                                fontSize = 15.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                            
                            // Percentage
                            Text(
                                text = "%.1f%%".format(item.percentage),
                                fontSize = 15.sp,
                                color = Color.Black,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                        
                        // Divider giữa các rows (trừ row cuối)
                        if (index < chartData.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = Color(0xFFF5F5F5),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * List Tab - Hiển thị danh sách lịch sử uống thuốc
 */
@Composable
private fun ListTab(
    historyList: List<HistoryItem>,
    modifier: Modifier = Modifier
) {
    if (historyList.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No history available",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(historyList) { item ->
                HistoryListItem(
                    historyItem = item
                )
                if (item != historyList.last()) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

/**
 * History List Item
 */
@Composable
private fun HistoryListItem(
    historyItem: HistoryItem
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val dateString = dateFormat.format(historyItem.history.takenDate)
    
    val medicine = historyItem.medicine
    val medicineName = medicine?.name ?: "Unknown"
    val quantity = medicine?.quantity ?: 1
    val unit = medicine?.unit ?: "pills"
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Column 1: Date
        Text(
            text = dateString,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.widthIn(min = 100.dp),
            fontWeight = FontWeight.Normal
        )
        
        // Spacer giữa date và medicine name
        Spacer(modifier = Modifier.width(16.dp))
        
        // Column 2: Medicine Name
        Text(
            text = medicineName,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Normal
        )
        
        // Spacer giữa medicine name và dosage
        Spacer(modifier = Modifier.width(12.dp))
        
        // Column 3: Dosage with icon
        Row(
            modifier = Modifier.widthIn(min = 80.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_pills2),
                contentDescription = "Pills",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "$quantity $unit",
                fontSize = 15.sp,
                color = Color.Black,
                fontWeight = FontWeight.Normal
            )
        }
        
        // Spacer giữa dosage và time
        Spacer(modifier = Modifier.width(12.dp))
        
        // Column 4: Time
        Text(
            text = historyItem.history.takenTime,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.widthIn(min = 80.dp),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Normal
        )
    }
}
