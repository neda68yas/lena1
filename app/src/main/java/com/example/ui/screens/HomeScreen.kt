package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MealItem
import com.example.data.model.UserProfile
import com.example.ui.components.MacroProgressBar
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.AccentPink
import com.example.ui.theme.PastelPurpleContainer
import com.example.ui.theme.PastelPurpleLight
import com.example.ui.theme.PastelPurplePrimary
import com.example.ui.theme.SoftBlueContainer
import com.example.ui.theme.SoftBlueSecondary
import com.example.ui.theme.SoftGreen
import com.example.ui.theme.SoftGreenContainer
import com.example.ui.theme.SoftOrange
import com.example.ui.theme.SoftOrangeContainer
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    profile: UserProfile,
    todayMeals: List<MealItem>,
    onDeleteMeal: (Long) -> Unit
) {
    val totalCaloriesConsumed = todayMeals.sumOf { it.calories }
    val targetCalories = profile.targetCalories
    val remainingCalories = (targetCalories - totalCaloriesConsumed).coerceAtLeast(0)

    val totalProtein = todayMeals.sumOf { it.protein.toDouble() }.toFloat()
    val totalCarbs = todayMeals.sumOf { it.carbs.toDouble() }.toFloat()
    val totalFat = todayMeals.sumOf { it.fat.toDouble() }.toFloat()

    // Targets estimated based on calorie distribution (30% P, 45% C, 25% F)
    val targetProtein = (targetCalories * 0.25f / 4f).coerceAtLeast(50f)
    val targetCarbs = (targetCalories * 0.50f / 4f).coerceAtLeast(120f)
    val targetFat = (targetCalories * 0.25f / 9f).coerceAtLeast(40f)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F6FC))
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            // Header Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(PastelPurpleContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👑",
                            fontSize = 24.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "سلام ${profile.name}، خوش اومدی عزیز دلم 🌸",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "هدف امروز: ${profile.goal} (${profile.targetWeightKg.toPersianDigits(1)} کیلوگرم)",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Calorie Summary Ring/Card
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = "Calories",
                                tint = SoftOrange,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "کالری‌شماری امروز",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(SoftOrangeContainer)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "هدف: ${targetCalories.toPersianDigits()} کالری",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SoftOrange
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CalorieStatItem(
                            title = "مصرف شده",
                            value = "${totalCaloriesConsumed.toPersianDigits()} کالری",
                            color = PastelPurplePrimary
                        )
                        CalorieStatItem(
                            title = "باقی مانده",
                            value = "${remainingCalories.toPersianDigits()} کالری",
                            color = SoftBlueSecondary
                        )
                        CalorieStatItem(
                            title = "هدف روزانه",
                            value = "${targetCalories.toPersianDigits()} کالری",
                            color = SoftGreen
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    val progress = if (targetCalories > 0) (totalCaloriesConsumed.toFloat() / targetCalories).coerceIn(0f, 1f) else 0f
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        color = if (totalCaloriesConsumed > targetCalories) AccentPink else PastelPurplePrimary,
                        trackColor = Color(0xFFF0EBF8)
                    )
                }
            }
        }

        // BMI Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(SoftBlueContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "BMI",
                            tint = SoftBlueSecondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "شاخص توده بدنی (BMI)",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile.bmi.toPersianDigits(1),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when (profile.bmiCategory) {
                                            "وزن طبیعی" -> SoftGreenContainer
                                            "کم‌وزن" -> SoftBlueContainer
                                            else -> SoftOrangeContainer
                                        }
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = profile.bmiCategory,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = when (profile.bmiCategory) {
                                        "وزن طبیعی" -> SoftGreen
                                        "کم‌وزن" -> SoftBlueSecondary
                                        else -> SoftOrange
                                    }
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "قد: ${profile.heightCm.toInt().toPersianDigits()} cm",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "وزن: ${profile.weightKg.toPersianDigits(1)} kg",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Daily Macros Breakdown
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
                    Text(
                        text = "درشت‌مغذی‌های مصرفی امروز",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    MacroProgressBar(
                        label = "پروتئین 🥩",
                        currentValue = totalProtein,
                        targetValue = targetProtein,
                        unit = "گرم",
                        color = AccentPink,
                        backgroundColor = Color(0xFFFCE4EC)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MacroProgressBar(
                        label = "کربوهیدرات 🍞",
                        currentValue = totalCarbs,
                        targetValue = targetCarbs,
                        unit = "گرم",
                        color = SoftBlueSecondary,
                        backgroundColor = SoftBlueContainer
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MacroProgressBar(
                        label = "چربی 🥑",
                        currentValue = totalFat,
                        targetValue = targetFat,
                        unit = "گرم",
                        color = SoftOrange,
                        backgroundColor = SoftOrangeContainer
                    )
                }
            }
        }

        // Today's Meal List Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = "Meals",
                    tint = PastelPurplePrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "وعده‌های غذایی امروز",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }
        }

        if (todayMeals.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🍽️",
                            fontSize = 32.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "امروز هنوز هیچ وعده غذایی ثبت نکرده‌ای.",
                            fontSize = 14.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = "از بخش «ثبت غذا» عکس غذاتو آپلود کن تا کالریش حساب بشه!",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(todayMeals, key = { it.id }) { meal ->
                MealItemCard(meal = meal, onDelete = { onDeleteMeal(meal.id) })
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CalorieStatItem(
    title: String,
    value: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun MealItemCard(
    meal: MealItem,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PastelPurpleContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (meal.mealType) {
                        "صبحانه" -> "🍳"
                        "ناهار" -> "🍲"
                        "شام" -> "🥗"
                        else -> "🍎"
                    },
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${meal.mealType} • ${meal.calories.toPersianDigits()} کالری",
                    fontSize = 13.sp,
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
