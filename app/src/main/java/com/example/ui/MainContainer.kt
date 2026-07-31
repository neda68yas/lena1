package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CuteEndearmentToast
import com.example.ui.screens.FoodLogScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.theme.PastelPurpleContainer
import com.example.ui.theme.PastelPurplePrimary
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

sealed class NavTab(val title: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector) {
    object Home : NavTab("خانه", Icons.Filled.Home, Icons.Outlined.Home)
    object FoodLog : NavTab("ثبت غذا", Icons.Filled.Restaurant, Icons.Outlined.Restaurant)
    object Progress : NavTab("پیشرفت", Icons.Filled.ShowChart, Icons.Outlined.ShowChart)
    object Profile : NavTab("پروفایل", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainContainer(viewModel: MainViewModel) {
    val profile by viewModel.userProfile.collectAsState()
    val todayMeals by viewModel.todayMeals.collectAsState()
    val monthlyMeals by viewModel.monthlyMeals.collectAsState()
    val weightEntries by viewModel.weightEntries.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val analysisResult by viewModel.analysisResult.collectAsState()
    val toastMessage by viewModel.cuteToast.collectAsState(initial = null)

    var selectedTabItem by remember { mutableIntStateOf(0) }
    val tabs = listOf(NavTab.Home, NavTab.FoodLog, NavTab.Progress, NavTab.Profile)

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        if (!profile.isOnboarded) {
            OnboardingScreen(
                initialProfile = profile,
                onComplete = { newProfile ->
                    viewModel.saveProfile(newProfile)
                    viewModel.updateOnboardingStatus(true)
                    viewModel.triggerRandomAffection()
                }
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Scaffold(
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color.White,
                            tonalElevation = 8.dp,
                            modifier = Modifier
                                .shadow(12.dp, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        ) {
                            tabs.forEachIndexed { index, tab ->
                                val isSelected = selectedTabItem == index
                                NavigationBarItem(
                                    selected = isSelected,
                                    onClick = {
                                        if (selectedTabItem != index) {
                                            selectedTabItem = index
                                        }
                                        viewModel.triggerRandomAffection()
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                            contentDescription = tab.title,
                                            tint = if (isSelected) PastelPurplePrimary else TextSecondary
                                        )
                                    },
                                    label = {
                                        Text(
                                            text = tab.title,
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) PastelPurplePrimary else TextSecondary
                                        )
                                    },
                                    colors = NavigationBarItemDefaults.colors(
                                        indicatorColor = PastelPurpleContainer
                                    )
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = selectedTabItem, label = "TabCrossfade") { tabIndex ->
                            when (tabIndex) {
                                0 -> HomeScreen(
                                    profile = profile,
                                    todayMeals = todayMeals,
                                    onDeleteMeal = { viewModel.deleteMeal(it) }
                                )
                                1 -> FoodLogScreen(
                                    isAnalyzing = isAnalyzing,
                                    analysisResult = analysisResult,
                                    onImageSelected = { uri -> viewModel.analyzeImage(uri) },
                                    onAddMeal = { name, cals, p, c, f, mealType, img ->
                                        viewModel.addMeal(name, cals, p, c, f, mealType, img)
                                    },
                                    onClearAnalysis = { viewModel.clearAnalysis() }
                                )
                                2 -> ProgressScreen(
                                    profile = profile,
                                    weightEntries = weightEntries,
                                    monthlyMeals = monthlyMeals,
                                    onAddWeight = { w, note -> viewModel.addWeightEntry(w, note) },
                                    onDeleteWeight = { viewModel.deleteWeightEntry(it) }
                                )
                                3 -> ProfileScreen(
                                    profile = profile,
                                    onSaveProfile = { updated -> viewModel.saveProfile(updated) },
                                    onResetOnboarding = { viewModel.updateOnboardingStatus(false) }
                                )
                            }
                        }
                    }
                }

                // Affection Toast Popup at Top
                CuteEndearmentToast(
                    message = toastMessage,
                    onDismiss = { },
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}
