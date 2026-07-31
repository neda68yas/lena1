package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.db.AppDatabase
import com.example.data.model.MealItem
import com.example.data.model.UserProfile
import com.example.data.model.WeightEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository(context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val mealDao = db.mealDao()
    private val weightDao = db.weightDao()

    private val prefs: SharedPreferences = context.getSharedPreferences("googooli_user_prefs", Context.MODE_PRIVATE)

    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    fun loadUserProfile(): UserProfile {
        val name = prefs.getString("name", "لنا") ?: "لنا"
        val age = prefs.getInt("age", 24)
        val gender = prefs.getString("gender", "زن") ?: "زن"
        val heightCm = prefs.getFloat("heightCm", 165f)
        val weightKg = prefs.getFloat("weightKg", 62f)
        val activityLevel = prefs.getString("activityLevel", "متوسط") ?: "متوسط"
        val goal = prefs.getString("goal", "حفظ وزن") ?: "حفظ وزن"
        val targetWeightKg = prefs.getFloat("targetWeightKg", 58f)
        val isOnboarded = prefs.getBoolean("isOnboarded", false)

        return UserProfile(
            name = name,
            age = age,
            gender = gender,
            heightCm = heightCm,
            weightKg = weightKg,
            activityLevel = activityLevel,
            goal = goal,
            targetWeightKg = targetWeightKg,
            isOnboarded = isOnboarded
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("name", profile.name)
            putInt("age", profile.age)
            putString("gender", profile.gender)
            putFloat("heightCm", profile.heightCm)
            putFloat("weightKg", profile.weightKg)
            putString("activityLevel", profile.activityLevel)
            putString("goal", profile.goal)
            putFloat("targetWeightKg", profile.targetWeightKg)
            putBoolean("isOnboarded", profile.isOnboarded)
            apply()
        }
        _userProfile.value = profile
    }

    // Today's date string YYYY-MM-DD
    fun getTodayDateString(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return formatter.format(Date())
    }

    // Current month prefix string YYYY-MM
    fun getCurrentMonthPrefix(): String {
        val formatter = SimpleDateFormat("yyyy-MM", Locale.US)
        return formatter.format(Date())
    }

    fun getTodayMeals(): Flow<List<MealItem>> = mealDao.getMealsForDate(getTodayDateString())

    fun getAllMeals(): Flow<List<MealItem>> = mealDao.getAllMeals()

    fun getMealsForMonth(monthPrefix: String): Flow<List<MealItem>> = mealDao.getMealsForMonth(monthPrefix)

    suspend fun insertMeal(meal: MealItem) = mealDao.insertMeal(meal)

    suspend fun deleteMeal(id: Long) = mealDao.deleteMealById(id)

    fun getAllWeightEntries(): Flow<List<WeightEntry>> = weightDao.getAllWeightEntries()

    suspend fun insertWeightEntry(entry: WeightEntry) {
        weightDao.insertWeightEntry(entry)
        // Update user's current weight in profile too
        val current = _userProfile.value
        saveUserProfile(current.copy(weightKg = entry.weightKg))
    }

    suspend fun deleteWeightEntry(id: Long) = weightDao.deleteWeightEntryById(id)
}
