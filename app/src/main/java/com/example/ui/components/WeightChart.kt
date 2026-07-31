package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeightEntry
import com.example.ui.theme.PastelPurplePrimary
import com.example.ui.theme.SoftBlueSecondary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun WeightChart(
    entries: List<WeightEntry>,
    targetWeightKg: Float,
    modifier: Modifier = Modifier
) {
    if (entries.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "هنوز وزنی ثبت نشده است. اولین وزن خود را ثبت کنید!",
                color = TextSecondary,
                fontSize = 14.sp
            )
        }
        return
    }

    val sortedEntries = entries.sortedBy { it.timestamp }
    val weights = sortedEntries.map { it.weightKg }
    val minWeight = (weights.minOrNull() ?: 50f).coerceAtMost(targetWeightKg) - 3f
    val maxWeight = (weights.maxOrNull() ?: 80f).coerceAtLeast(targetWeightKg) + 3f
    val range = (maxWeight - minWeight).coerceAtLeast(1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .padding(18.dp)
    ) {
        Column {
            Text(
                text = "نمودار روند وزن (کیلوگرم)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PastelPurplePrimary,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val width = size.width
                val height = size.height

                // Draw background horizontal lines
                val steps = 4
                for (i in 0..steps) {
                    val y = height * (i.toFloat() / steps)
                    drawLine(
                        color = Color(0xFFF0EBF8),
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                // Target weight line (dashed soft blue)
                val targetY = height - ((targetWeightKg - minWeight) / range * height)
                drawLine(
                    color = SoftBlueSecondary.copy(alpha = 0.7f),
                    start = Offset(0f, targetY),
                    end = Offset(width, targetY),
                    strokeWidth = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f))
                )

                if (sortedEntries.size == 1) {
                    // Single point
                    val point = Offset(width / 2f, height - ((sortedEntries[0].weightKg - minWeight) / range * height))
                    drawCircle(
                        color = PastelPurplePrimary,
                        radius = 7.dp.toPx(),
                        center = point
                    )
                } else {
                    val points = sortedEntries.mapIndexed { index, entry ->
                        val x = (index.toFloat() / (sortedEntries.size - 1)) * width
                        val y = height - ((entry.weightKg - minWeight) / range * height)
                        Offset(x, y)
                    }

                    // Path for line
                    val strokePath = Path().apply {
                        moveTo(points.first().x, points.first().y)
                        for (i in 0 until points.size - 1) {
                            val p1 = points[i]
                            val p2 = points[i + 1]
                            val controlPoint1 = Offset(p1.x + (p2.x - p1.x) / 2f, p1.y)
                            val controlPoint2 = Offset(p1.x + (p2.x - p1.x) / 2f, p2.y)
                            cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p2.x, p2.y)
                        }
                    }

                    // Fill gradient
                    val fillPath = Path().apply {
                        addPath(strokePath)
                        lineTo(points.last().x, height)
                        lineTo(points.first().x, height)
                        close()
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                PastelPurplePrimary.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )

                    drawPath(
                        path = strokePath,
                        color = PastelPurplePrimary,
                        style = Stroke(width = 3.5.dp.toPx())
                    )

                    // Draw circles
                    points.forEach { point ->
                        drawCircle(
                            color = Color.White,
                            radius = 6.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = PastelPurplePrimary,
                            radius = 4.dp.toPx(),
                            center = point
                        )
                    }
                }
            }
        }
    }
}
