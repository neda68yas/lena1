package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val calories: Int,
    val protein: Float = 0f,
    val carbs: Float = 0f,
    val fat: Float = 0f,
    val imageUri: String? = null,
    val mealType: String = "ناهار", // "صبحانه", "ناهار", "شام", "میان‌وعده"
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
