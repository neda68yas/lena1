package com.example.data.model

import kotlin.math.roundToInt

data class UserProfile(
    val name: String = "لنا",
    val age: Int = 24,
    val gender: String = "زن", // "زن" یا "مرد"
    val heightCm: Float = 165f,
    val weightKg: Float = 62f,
    val activityLevel: String = "متوسط", // "کم", "متوسط", "زیاد", "بسیار زیاد"
    val goal: String = "حفظ وزن", // "کاهش وزن", "حفظ وزن", "افزایش وزن"
    val targetWeightKg: Float = 58f,
    val isOnboarded: Boolean = false
) {
    val bmi: Float
        get() {
            val hMeter = heightCm / 100f
            if (hMeter <= 0) return 0f
            return weightKg / (hMeter * hMeter)
        }

    val bmiCategory: String
        get() = when {
            bmi < 18.5f -> "کم‌وزن"
            bmi < 25.0f -> "وزن طبیعی"
            bmi < 30.0f -> "اضافه وزن"
            else -> "چاقی"
        }

    val bmr: Float
        get() {
            val isFemale = gender.trim() == "زن" || gender.contains("زن")
            return if (isFemale) {
                (10f * weightKg) + (6.25f * heightCm) - (5f * age) - 161f
            } else {
                (10f * weightKg) + (6.25f * heightCm) - (5f * age) + 5f
            }
        }

    val activityMultiplier: Float
        get() = when (activityLevel) {
            "کم" -> 1.2f
            "زیاد" -> 1.55f
            "بسیار زیاد" -> 1.725f
            else -> 1.375f // "متوسط"
        }

    val tdee: Float
        get() = bmr * activityMultiplier

    val targetCalories: Int
        get() = when (goal) {
            "کاهش وزن" -> (tdee - 500f).coerceAtLeast(1200f).roundToInt()
            "افزایش وزن" -> (tdee + 400f).roundToInt()
            else -> tdee.roundToInt() // "حفظ وزن"
        }
}
