package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.AccentPink
import com.example.ui.theme.PastelPurpleContainer
import com.example.ui.theme.PastelPurplePrimary
import com.example.ui.theme.SoftBlueSecondary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    initialProfile: UserProfile,
    onComplete: (UserProfile) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    val totalSteps = 8

    var name by remember { mutableStateOf(initialProfile.name.ifBlank { "لنا" }) }
    var ageStr by remember { mutableStateOf(initialProfile.age.toString()) }
    var gender by remember { mutableStateOf(initialProfile.gender) }
    var heightStr by remember { mutableStateOf(initialProfile.heightCm.toInt().toString()) }
    var weightStr by remember { mutableStateOf(initialProfile.weightKg.toString()) }
    var activityLevel by remember { mutableStateOf(initialProfile.activityLevel) }
    var goal by remember { mutableStateOf(initialProfile.goal) }
    var targetWeightStr by remember { mutableStateOf(initialProfile.targetWeightKg.toString()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F6FE))
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Greeting Banner
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(30.dp))
                    .background(PastelPurpleContainer)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Love",
                        tint = AccentPink,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "سلام عشق من! 🌸",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PastelPurplePrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Step Progress
            LinearProgressIndicator(
                progress = { step.toFloat() / totalSteps },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = PastelPurplePrimary,
                trackColor = Color(0xFFE8DEF8)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "مرحله ${step.toPersianDigits()} از ${totalSteps.toPersianDigits()}",
                fontSize = 12.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Content Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedContent(
                        targetState = step,
                        transitionSpec = {
                            slideInHorizontally { width -> if (targetState > initialState) -width else width } + fadeIn() togetherWith
                                    slideOutHorizontally { width -> if (targetState > initialState) width else -width } + fadeOut()
                        },
                        label = "StepTransition"
                    ) { currentStep ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            when (currentStep) {
                                1 -> {
                                    Text(
                                        text = "اسمت چیه عزیز دلم؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "دوست داری توی اپ چطوری صدات کنم؟",
                                        fontSize = 14.sp,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    OutlinedTextField(
                                        value = name,
                                        onValueChange = { name = it },
                                        label = { Text("نام شما") },
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = PastelPurplePrimary,
                                            unfocusedBorderColor = Color(0xFFE0E0E0)
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                2 -> {
                                    Text(
                                        text = "چند سالته؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "برای محاسبه دقیق سوخت‌وساز بدنت لازمش داریم.",
                                        fontSize = 14.sp,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    OutlinedTextField(
                                        value = ageStr,
                                        onValueChange = { ageStr = it.filter { char -> char.isDigit() } },
                                        label = { Text("سن (سال)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = PastelPurplePrimary,
                                            unfocusedBorderColor = Color(0xFFE0E0E0)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                3 -> {
                                    Text(
                                        text = "جنسیتت چیه؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        SelectionOptionCard(
                                            title = "زن 👩‍🦰",
                                            isSelected = gender == "زن",
                                            onClick = { gender = "زن" },
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        SelectionOptionCard(
                                            title = "مرد 👨",
                                            isSelected = gender == "مرد",
                                            onClick = { gender = "مرد" },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }

                                4 -> {
                                    Text(
                                        text = "قدت چقدره؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "به سانتی‌متر وارد کن عزیز دلم.",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    OutlinedTextField(
                                        value = heightStr,
                                        onValueChange = { heightStr = it.filter { char -> char.isDigit() } },
                                        label = { Text("قد (سانتی‌متر)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = PastelPurplePrimary,
                                            unfocusedBorderColor = Color(0xFFE0E0E0)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                5 -> {
                                    Text(
                                        text = "وزن فعلیت چقدره؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "به کیلوگرم وارد کن.",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    OutlinedTextField(
                                        value = weightStr,
                                        onValueChange = { weightStr = it },
                                        label = { Text("وزن فعلی (کیلوگرم)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = PastelPurplePrimary,
                                            unfocusedBorderColor = Color(0xFFE0E0E0)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                6 -> {
                                    Text(
                                        text = "میزان فعالیت روزانه‌ات چطوره؟",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        listOf("کم", "متوسط", "زیاد", "بسیار زیاد").forEach { act ->
                                            SelectionOptionCard(
                                                title = when (act) {
                                                    "کم" -> "کم (پشت میز نشینی / پیاده‌روی کم)"
                                                    "متوسط" -> "متوسط (ورزش ۱ تا ۳ بار در هفته)"
                                                    "زیاد" -> "زیاد (ورزش ۳ تا ۵ بار در هفته)"
                                                    else -> "بسیار زیاد (ورزش سنگین هر روز)"
                                                },
                                                isSelected = activityLevel == act,
                                                onClick = { activityLevel = act },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }

                                7 -> {
                                    Text(
                                        text = "هدفت چیه گوگولی؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        listOf("کاهش وزن", "حفظ وزن", "افزایش وزن").forEach { g ->
                                            SelectionOptionCard(
                                                title = when (g) {
                                                    "کاهش وزن" -> "کاهش وزن 📉"
                                                    "حفظ وزن" -> "حفظ وزن ⚖️"
                                                    else -> "افزایش وزن 📈"
                                                },
                                                isSelected = goal == g,
                                                onClick = { goal = g },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }

                                8 -> {
                                    Text(
                                        text = "وزن هدفت چقدره؟",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "دوست داری به چه وزنی برسی؟ (کیلوگرم)",
                                        fontSize = 14.sp,
                                        color = TextSecondary,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    OutlinedTextField(
                                        value = targetWeightStr,
                                        onValueChange = { targetWeightStr = it },
                                        label = { Text("وزن هدف (کیلوگرم)") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = PastelPurplePrimary,
                                            unfocusedBorderColor = Color(0xFFE0E0E0)
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (step > 1) {
                            Button(
                                onClick = { step-- },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF3E8FF),
                                    contentColor = PastelPurplePrimary
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "قبلی"
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("قبلی", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        Button(
                            onClick = {
                                if (step < totalSteps) {
                                    step++
                                } else {
                                    val parsedAge = ageStr.toIntOrNull() ?: 24
                                    val parsedHeight = heightStr.toFloatOrNull() ?: 165f
                                    val parsedWeight = weightStr.toFloatOrNull() ?: 62f
                                    val parsedTargetWeight = targetWeightStr.toFloatOrNull() ?: parsedWeight

                                    val finalProfile = UserProfile(
                                        name = name.ifBlank { "لنا" },
                                        age = parsedAge,
                                        gender = gender,
                                        heightCm = parsedHeight,
                                        weightKg = parsedWeight,
                                        activityLevel = activityLevel,
                                        goal = goal,
                                        targetWeightKg = parsedTargetWeight,
                                        isOnboarded = true
                                    )
                                    onComplete(finalProfile)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PastelPurplePrimary),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Text(
                                text = if (step == totalSteps) "شروع اپلیکیشن ✨" else "بعدی",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                            if (step < totalSteps) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "بعدی",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionOptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PastelPurpleContainer else Color(0xFFFAFAFA))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PastelPurplePrimary else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) PastelPurplePrimary else TextPrimary,
            textAlign = TextAlign.Center
        )
    }
}
