package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PastelPurplePrimary,
    onPrimary = Color.White,
    primaryContainer = PastelPurpleContainer,
    onPrimaryContainer = PastelPurplePrimary,
    secondary = SoftBlueSecondary,
    onSecondary = Color.White,
    secondaryContainer = SoftBlueContainer,
    onSecondaryContainer = SoftBlueSecondary,
    tertiary = AccentPink,
    onTertiary = Color.White,
    tertiaryContainer = AccentPinkContainer,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceWhite,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary,
    outline = CardBorderLight
)

@Composable
fun GoogooliLenaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
