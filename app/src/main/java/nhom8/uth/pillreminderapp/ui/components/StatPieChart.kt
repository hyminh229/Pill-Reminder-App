package nhom8.uth.pillreminderapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Data class cho chart segment
 */
data class ChartSegment(
    val value: Float,
    val color: Color
)

/**
 * Donut chart component
 */
@Composable
fun StatPieChart(
    segments: List<ChartSegment>,
    totalValue: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(250.dp)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawDonutChart(segments)
        }
        
        // Total value ở giữa
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Value",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = totalValue.toString(),
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Vẽ donut chart
 */
private fun DrawScope.drawDonutChart(segments: List<ChartSegment>) {
    if (segments.isEmpty()) return
    
    val total = segments.sumOf { it.value.toDouble() }.toFloat()
    if (total == 0f) return
    
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 * 0.8f
    val innerRadius = radius * 0.5f // Donut hole
    
    var startAngle = -90f // Bắt đầu từ trên
    
    segments.forEach { segment ->
        val sweepAngle = (segment.value / total) * 360f
        
        // Vẽ segment (donut chart)
        drawArc(
            color = segment.color,
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = radius - innerRadius
            )
        )
        
        startAngle += sweepAngle
    }
}
