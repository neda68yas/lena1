package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealItem
import com.example.data.model.UserProfile
import com.example.data.model.WeightEntry
import com.example.ui.components.WeightChart
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.PastelPurpleContainer
import com.example.ui.theme.PastelPurplePrimary
import com.example.ui.theme.SoftBlueContainer
import com.example.ui.theme.SoftBlueSecondary
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftGreenContainer
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(
    profile: UserProfile,
    weightEntries: List<WeightEntry>,
    monthlyMeals: List<MealItem>,
    onAddWeight: (Float, String?) -> Unit,
    onDeleteWeight: (Long) -> Unit
) {
    var newWeightInput by remember { mutableStateOf("") }
    var newWeightNote by remember { mutableStateOf("") }

    val sortedWeights = weightEntries.sortedBy { it.timestamp }
    val initialWeight = sortedWeights.firstOrNull()?.weightKg ?: profile.weightKg
    val latestWeight = sortedWeights.lastOrNull()?.weightKg ?: profile.weightKg
    val weightDifference = latestWeight - initialWeight

    // Monthly Calorie Statistics
    val totalMonthlyCalories = monthlyMeals.sumOf { it.calories }
    // Distinct days logged this month
    val distinctDaysCount = monthlyMeals.map { it.date }.distinct().size.coerceAtLeast(1)
    val avgDailyCalories = totalMonthlyCalories / distinctDaysCount

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FC))
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Title
            Text(
                text = "پیشرفت و روند وزن 📈",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Weight Progress Chart Card
        item {
            WeightChart(
                entries = weightEntries,
                targetWeightKg = profile.targetWeightKg
            )
        }

        // Monthly Summary Report Card (گزارش ماهانه)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(PastelPurpleContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Assessment,
                                contentDescription = "Report",
                                tint = PastelPurplePrimary
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "گزارش عملکرد این ماه 📊",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "خلاصه وضعیت کالری و وزن ${profile.name}",
                                fontSize = 12.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ReportStatBox(
                            title = "مجموع کالری ماه",
                            value = "${totalMonthlyCalories.toPersianDigits()} kcal",
                            color = PastelPurplePrimary,
                            bgColor = PastelPurpleContainer
                        )
                        ReportStatBox(
                            title = "میانگین روزانه",
                            value = "${avgDailyCalories.toPersianDigits()} kcal",
                            color = SoftBlueSecondary,
                            bgColor = SoftBlueContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Weight difference highlight
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                if (weightDifference <= 0) SoftGreenContainer else Color(0xFFFFF3E0)
                            )
                            .padding(14.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when {
                                weightDifference < 0 -> "عزیز دلم! نسبت به شروع این ماه ${(-weightDifference).toPersianDigits(1)} کیلوگرم وزن کم کردی! 🎉"
                                weightDifference > 0 -> "نسبت به شروع این ماه ${weightDifference.toPersianDigits(1)} کیلوگرم وزن اضافه کردی! 💪"
                                else -> "وزن شما نسبت به شروع ماه کاملاً ثابت بوده است. ✨"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (weightDifference <= 0) SoftGreen else Color(0xFFE65100)
                        )
                    }
                }
            }
        }

        // New Weight Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Scale,
                            contentDescription = "Weight",
                            tint = PastelPurplePrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ثبت وزن جدید ⚖️",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = newWeightInput,
                            onValueChange = { newWeightInput = it },
                            label = { Text("وزن جدید (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PastelPurplePrimary,
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                val w = newWeightInput.toFloatOrNull()
                                if (w != null && w > 20f) {
                                    onAddWeight(w, newWeightNote.ifBlank { null })
                                    newWeightInput = ""
                                    newWeightNote = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PastelPurplePrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(54.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ثبت", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // Weight History List Header
        item {
            Text(
                text = "تاریخچه ثبت وزن",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        if (weightEntries.isEmpty()) {
            item {
                Text(
                    text = "هنوز وزنی ثبت نشده است.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }
        } else {
            items(weightEntries.sortedByDescending { it.timestamp }, key = { it.id }) { entry ->
                WeightEntryRow(entry = entry, onDelete = { onDeleteWeight(entry.id) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ReportStatBox(
    title: String,
    value: String,
    color: Color,
    bgColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = title, fontSize = 12.sp, color = TextSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun WeightEntryRow(
    entry: WeightEntry,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${entry.weightKg.toPersianDigits(1)} کیلوگرم",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = entry.date.toPersianDigits(),
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = Color(0xFFE57373)
                )
            }
        }
    }
}
