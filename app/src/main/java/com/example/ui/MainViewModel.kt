package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.MealItem
import com.example.data.model.UserProfile
import com.example.data.model.WeightEntry
import com.example.data.remote.FoodAnalysisResult
import com.example.data.remote.GeminiFoodAnalyzer
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val foodAnalyzer = GeminiFoodAnalyzer(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfile

    val todayMeals: StateFlow<List<MealItem>> = repository.getTodayMeals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val monthlyMeals: StateFlow<List<MealItem>> = repository.getMealsForMonth(repository.getCurrentMonthPrefix())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val weightEntries: StateFlow<List<WeightEntry>> = repository.getAllWeightEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    private val _analysisResult = MutableStateFlow<FoodAnalysisResult?>(null)
    val analysisResult: StateFlow<FoodAnalysisResult?> = _analysisResult.asStateFlow()

    private val _cuteToast = MutableSharedFlow<String>()
    val cuteToast: SharedFlow<String> = _cuteToast.asSharedFlow()

    private val affectionatePhrases = listOf(
        "اووووف",
        "جیگرتو خوردیده بودم اخه",
        "نفس منی اخه خانوووووم",
        "میمیرم براتا",
        "گوگولیه منی",
        "تو چرا هر روز گوگولی تر میشی اخه خانوووم",
        "لنا هر روز گوگولی تر از دیروز"
    )

    init {
        // Seed initial weight entry if empty and onboarded
        viewModelScope.launch {
            val weights = repository.getAllWeightEntries().firstOrNull()
            if (weights.isNullOrEmpty()) {
                val currentWeight = userProfile.value.weightKg
                repository.insertWeightEntry(
                    WeightEntry(
                        weightKg = currentWeight,
                        date = repository.getTodayDateString(),
                        note = "وزن اولیه ثبت شده"
                    )
                )
            }
        }
    }

    fun triggerRandomAffection() {
        viewModelScope.launch {
            val phrase = affectionatePhrases.random()
            _cuteToast.emit(phrase)
        }
    }

    fun saveProfile(profile: UserProfile) {
        repository.saveUserProfile(profile)
    }

    fun updateOnboardingStatus(completed: Boolean) {
        val current = userProfile.value
        saveProfile(current.copy(isOnboarded = completed))
    }

    fun analyzeImage(imageUri: Uri) {
        viewModelScope.launch {
            _isAnalyzing.value = true
            _analysisResult.value = null
            val result = foodAnalyzer.analyzeFoodImage(imageUri)
            _isAnalyzing.value = false
            result.onSuccess {
                _analysisResult.value = it
            }.onFailure {
                _analysisResult.value = null
            }
        }
    }

    fun clearAnalysis() {
        _analysisResult.value = null
    }

    fun addMeal(
        name: String,
        calories: Int,
        protein: Float = 0f,
        carbs: Float = 0f,
        fat: Float = 0f,
        mealType: String = "ناهار",
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            val meal = MealItem(
                name = name,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fat = fat,
                mealType = mealType,
                imageUri = imageUri,
                date = repository.getTodayDateString()
            )
            repository.insertMeal(meal)
            _analysisResult.value = null
            triggerRandomAffection()
        }
    }

    fun deleteMeal(id: Long) {
        viewModelScope.launch {
            repository.deleteMeal(id)
        }
    }

    fun addWeightEntry(weightKg: Float, note: String? = null) {
        viewModelScope.launch {
            val entry = WeightEntry(
                weightKg = weightKg,
                date = repository.getTodayDateString(),
                note = note
            )
            repository.insertWeightEntry(entry)
            triggerRandomAffection()
        }
    }

    fun deleteWeightEntry(id: Long) {
        viewModelScope.launch {
            repository.deleteWeightEntry(id)
        }
    }
}
