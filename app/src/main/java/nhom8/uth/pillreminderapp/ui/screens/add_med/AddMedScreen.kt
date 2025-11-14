package nhom8.uth.pillreminderapp.ui.screens.add_med

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collect
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.theme.DarkBlue
import nhom8.uth.pillreminderapp.ui.theme.LightBlue
import java.text.SimpleDateFormat
import java.util.*

/**
 * Màn hình thêm/sửa thuốc
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedScreen(
    viewModel: AddMedViewModel = hiltViewModel(),
    medicineId: Long? = null,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    // Load medicine if editing
    LaunchedEffect(medicineId) {
        if (medicineId != null) {
            viewModel.loadMedicine(medicineId)
        }
    }
    
    val medicineName by viewModel.medicineName.collectAsState()
    val quantity by viewModel.quantity.collectAsState()
    val unit by viewModel.unit.collectAsState()
    val intakeAdvice by viewModel.intakeAdvice.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val reminderTimes by viewModel.reminderTimes.collectAsState()
    val repeat by viewModel.repeat.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val reminderTimesError by viewModel.reminderTimesError.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Date picker states
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    
    // Dropdown states
    var unitExpanded by remember { mutableStateOf(false) }
    var intakeAdviceExpanded by remember { mutableStateOf(false) }
    var repeatExpanded by remember { mutableStateOf(false) }
    var startDateOptionExpanded by remember { mutableStateOf(false) }
    var showRepeatPicker by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Medication details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            painter = painterResource(id = R.drawable.angle_left_24),
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Pills name section
            item {
                Column {
                    Text(
                        text = "Pills name:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = medicineName,
                        onValueChange = { viewModel.updateName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Add name") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.pill_reminder_logo),
                                contentDescription = "Pill",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { /* Edit action */ }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )
                            }
                        },
                        isError = nameError != null,
                        supportingText = nameError?.let {
                            { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
            
            // Intake advice section
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Intake advice:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        
                        // Intake advice dropdown
                        Box {
                            OutlinedTextField(
                                value = intakeAdvice,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .width(150.dp)
                                    .clickable { intakeAdviceExpanded = true },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint = Color.Gray
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LightBlue,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                )
                            )
                            DropdownMenu(
                                expanded = intakeAdviceExpanded,
                                onDismissRequest = { intakeAdviceExpanded = false }
                            ) {
                                viewModel.intakeAdviceOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            viewModel.updateIntakeAdvice(option)
                                            intakeAdviceExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Quantity and Unit row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Quantity selector
                        OutlinedTextField(
                            value = quantity.toString(),
                            onValueChange = {
                                it.toIntOrNull()?.let { qty ->
                                    viewModel.updateQuantity(qty)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.pill_reminder_logo),
                                    contentDescription = "Quantity",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )
                        
                        // Unit dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = unit,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { unitExpanded = true },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint = Color.Gray
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LightBlue,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                )
                            )
                            DropdownMenu(
                                expanded = unitExpanded,
                                onDismissRequest = { unitExpanded = false }
                            ) {
                                viewModel.units.forEach { unitOption ->
                                    DropdownMenuItem(
                                        text = { Text(unitOption) },
                                        onClick = {
                                            viewModel.updateUnit(unitOption)
                                            unitExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Date section
            item {
                Column {
                    // Start date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Start date:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        
                        Box {
                            val startDateText = remember(startDate) {
                                val startCal = Calendar.getInstance().apply { time = startDate }
                                val todayCal = Calendar.getInstance()
                                when {
                                    isSameDay(startCal, todayCal) -> "Today"
                                    else -> formatDate(startDate)
                                }
                            }
                            
                            OutlinedTextField(
                                value = startDateText,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .width(150.dp)
                                    .clickable { startDateOptionExpanded = true },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown",
                                        tint = Color.Gray
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = LightBlue,
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                )
                            )
                            
                            DropdownMenu(
                                expanded = startDateOptionExpanded,
                                onDismissRequest = { startDateOptionExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Today") },
                                    onClick = {
                                        viewModel.setStartDateToToday()
                                        startDateOptionExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Select date") },
                                    onClick = {
                                        showStartDatePicker = true
                                        startDateOptionExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Due date
                    OutlinedTextField(
                        value = endDate?.let { formatDate(it) } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showEndDatePicker = true },
                        placeholder = { Text("Due date") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar_clock_24),
                                contentDescription = "Calendar",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
            
            // Notification section
            item {
                Column {
                    Text(
                        text = "Notification:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Reminder times list
                    if (reminderTimes.isNotEmpty()) {
                        reminderTimes.forEach { time ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedTextField(
                                    value = time,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.weight(1f),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Notifications,
                                            contentDescription = "Notification",
                                            tint = Color.Gray
                                        )
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = LightBlue,
                                        unfocusedBorderColor = Color(0xFFE0E0E0)
                                    )
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                IconButton(
                                    onClick = { viewModel.removeReminderTime(time) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = Color(0xFFE91E63)
                                    )
                                }
                            }
                        }
                    }
                    
                    if (reminderTimesError != null) {
                        Text(
                            text = reminderTimesError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Add reminder time button
                    OutlinedButton(
                        onClick = { showTimePicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DarkBlue
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add reminder",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add reminder time")
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Repeat picker button
                    OutlinedTextField(
                        value = repeat,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showRepeatPicker = true },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle_24),
                                contentDescription = "Repeat",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
            
            // Done button
            item {
                Button(
                    onClick = {
                        viewModel.saveMedicine(onSave)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkBlue
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "Done",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
    
    // Start date picker dialog
    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = startDate.time
        )
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.updateStartDate(Date(millis))
                        }
                        showStartDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
    
    // End date picker dialog
    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = endDate?.time ?: System.currentTimeMillis()
        )
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.updateEndDate(Date(millis))
                        }
                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
    
    // Time picker dialog
    if (showTimePicker) {
        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                val timeString = formatTime(hour, minute)
                viewModel.addReminderTime(timeString)
                showTimePicker = false
            }
        )
    }
    
    // Repeat picker dialog
    if (showRepeatPicker) {
        RepeatPickerDialog(
            currentRepeat = repeat,
            onDismissRequest = { showRepeatPicker = false },
            onRepeatSelected = { repeatValue ->
                viewModel.updateRepeat(repeatValue)
                showRepeatPicker = false
            }
        )
    }
}

/**
 * Format date to string
 */
private fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return sdf.format(date)
}

/**
 * Format time to 12-hour format with AM/PM
 */
private fun formatTime(hour: Int, minute: Int): String {
    val hour12 = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
    val amPm = if (hour < 12) "AM" else "PM"
    val minuteStr = String.format("%02d", minute)
    return "$hour12:$minuteStr $amPm"
}

/**
 * Helper function to check if two dates are the same day
 */
private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

/**
 * Material 3 Time Picker Dialog - follows Material Design 3 specifications
 * Reference: 
 * - https://m3.material.io/components/time-pickers/specs
 * - https://developer.android.com/develop/ui/compose/components/time-pickers
 * 
 * This implementation uses TimePicker (dial picker) for 12-hour format with AM/PM
 * Specifications:
 * - Dialog corner radius: 28dp
 * - Content padding: 24dp
 * - Button spacing: 8dp
 * - Uses MaterialTheme.colorScheme for theming
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit
) {
    val currentTime = Calendar.getInstance()
    
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false // 12-hour format with AM/PM
    )
    
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = androidx.compose.ui.window.DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Time Picker - Dial style
                // Material 3 TimePicker automatically handles spacing and layout
                TimePicker(
                    state = timePickerState,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                // Action buttons - Material 3 spec: buttons aligned to end
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(
                        onClick = {
                            onTimeSelected(timePickerState.hour, timePickerState.minute)
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

/**
 * Repeat Picker Dialog - allows user to select repeat frequency and days of week
 * Design based on Material Design 3 specifications
 */
@Composable
fun RepeatPickerDialog(
    currentRepeat: String,
    onDismissRequest: () -> Unit,
    onRepeatSelected: (String) -> Unit
) {
    // Parse current repeat value
    var repeatInterval by remember { mutableStateOf(1) }
    var repeatUnit by remember { mutableStateOf("week") } // day, week, month
    var selectedDays by remember { mutableStateOf(setOf<Int>()) }
    var unitExpanded by remember { mutableStateOf(false) }
    
    // Days of week in English
    val daysOfWeek = listOf(
        "Mon" to Calendar.MONDAY,
        "Tue" to Calendar.TUESDAY,
        "Wed" to Calendar.WEDNESDAY,
        "Thu" to Calendar.THURSDAY,
        "Fri" to Calendar.FRIDAY,
        "Sat" to Calendar.SATURDAY,
        "Sun" to Calendar.SUNDAY
    )
    
    val units = listOf("day", "week", "month")
    
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1E1E1E) // Dark background
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title
                Text(
                    text = "Repeat every...",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                // Repeat interval selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    // Number selector with increase/decrease buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { 
                                if (repeatInterval > 1) repeatInterval-- 
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.cross_circle_24),
                                contentDescription = "Decrease",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = repeatInterval.toString(),
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .background(
                                    color = Color.Transparent,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                        IconButton(
                            onClick = { 
                                if (repeatInterval < 99) repeatInterval++ 
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    // Unit dropdown
                    Box {
                        Row(
                            modifier = Modifier
                                .clickable { unitExpanded = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = repeatUnit,
                                fontSize = 18.sp,
                                color = Color.White,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Unit",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = unitExpanded,
                            onDismissRequest = { unitExpanded = false }
                        ) {
                            units.forEach { unit ->
                                DropdownMenuItem(
                                    text = { Text(unit, color = Color.Black) },
                                    onClick = {
                                        repeatUnit = unit
                                        unitExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Days of week selector - only show for "week"
                if (repeatUnit == "week") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    ) {
                        // Grid layout for days
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            daysOfWeek.take(4).forEachIndexed { index, (label, dayOfWeek) ->
                                DayButton(
                                    label = label,
                                    isSelected = selectedDays.contains(dayOfWeek),
                                    onClick = {
                                        selectedDays = if (selectedDays.contains(dayOfWeek)) {
                                            selectedDays - dayOfWeek
                                        } else {
                                            selectedDays + dayOfWeek
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Add spacer at start to center the 3 buttons
                            Spacer(modifier = Modifier.weight(1f))
                            daysOfWeek.drop(4).forEach { (label, dayOfWeek) ->
                                DayButton(
                                    label = label,
                                    isSelected = selectedDays.contains(dayOfWeek),
                                    onClick = {
                                        selectedDays = if (selectedDays.contains(dayOfWeek)) {
                                            selectedDays - dayOfWeek
                                        } else {
                                            selectedDays + dayOfWeek
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
                
                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest
                    ) {
                        Text(
                            "CANCEL",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    TextButton(
                        onClick = {
                            // Format repeat string
                            val daysStr = if (repeatUnit == "week" && selectedDays.isNotEmpty()) {
                                selectedDays.sorted().joinToString(",") { it.toString() }
                            } else {
                                ""
                            }
                            val repeatValue = "$repeatInterval $repeatUnit${if (daysStr.isNotEmpty()) ":$daysStr" else ""}"
                            onRepeatSelected(repeatValue)
                        }
                    ) {
                        Text(
                            "DONE",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

/**
 * Day button for repeat picker
 */
@Composable
private fun DayButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isSelected) Color(0xFFB0B0B0) else Color.Transparent,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = CircleShape
                )
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) Color(0xFF1E1E1E) else Color.White
        )
    }
}

