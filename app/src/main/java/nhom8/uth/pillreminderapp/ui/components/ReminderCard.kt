package nhom8.uth.pillreminderapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nhom8.uth.pillreminderapp.R
import nhom8.uth.pillreminderapp.ui.screens.home.ReminderStatus

/**
 * Component hiển thị thông tin một reminder
 */
@Composable
fun ReminderCard(
    medicineName: String,
    reminderTime: String,
    status: ReminderStatus,
    intakeAdvice: String = "None",
    onCardClick: () -> Unit = {},
    onCheckboxClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White, // White background for card
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onCardClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Pill icon
        Icon(
            painter = painterResource(id = R.drawable.ic_pill),
            contentDescription = "Pill",
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Medicine info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = medicineName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reminderTime,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Status badge with "-" prefix
                Text(
                    text = when (status) {
                        ReminderStatus.COMPLETED -> "- Completed"
                        ReminderStatus.BEFORE_EATING -> "- Before Eating"
                        ReminderStatus.SKIPPED -> "- Skipped"
                        ReminderStatus.MISSED -> "- Missed"
                        ReminderStatus.PENDING -> "- Pending"
                    },
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Checkbox/Checkmark on the right
        IconButton(
            onClick = onCheckboxClick,
            modifier = Modifier.size(40.dp)
        ) {
            if (status == ReminderStatus.COMPLETED) {
                // Checkmark in grey filled circle
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9E9E9E)), // Grey background
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                // Empty circle outline
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFF9E9E9E), CircleShape) // Grey border
                )
            }
        }
    }
}
