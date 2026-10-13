package com.fortytwo.swifty.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Neutral surfaces everywhere so the only color on screen is the accent
private val DarkColorScheme = darkColorScheme(
    primary = Teal42,
    onPrimary = Neutral05,
    primaryContainer = Neutral20,
    onPrimaryContainer = Teal42,
    secondary = Neutral70,
    onSecondary = Neutral05,
    secondaryContainer = Neutral20,
    onSecondaryContainer = Neutral90,
    tertiary = Neutral70,
    background = Neutral05,
    onBackground = Neutral90,
    surface = Neutral05,
    onSurface = Neutral90,
    surfaceVariant = Neutral20,
    onSurfaceVariant = Neutral70,
    surfaceContainerLowest = Color.Black,
    surfaceContainerLow = Neutral10,
    surfaceContainer = Neutral10,
    surfaceContainerHigh = Neutral15,
    surfaceContainerHighest = Neutral20,
    outline = Neutral50,
    outlineVariant = Neutral30,
)

private val LightColorScheme = lightColorScheme(
    primary = Teal42Dark,
    onPrimary = Color.White,
    primaryContainer = Neutral94,
    onPrimaryContainer = Teal42Dark,
    secondary = Neutral50,
    onSecondary = Color.White,
    secondaryContainer = Neutral90,
    onSecondaryContainer = Neutral10,
    tertiary = Neutral50,
    background = Neutral97,
    onBackground = Neutral10,
    surface = Neutral97,
    onSurface = Neutral10,
    surfaceVariant = Neutral90,
    onSurfaceVariant = Neutral50,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Neutral94,
    surfaceContainer = Neutral94,
    surfaceContainerHigh = Neutral90,
    surfaceContainerHighest = Neutral85,
    outline = Neutral60,
    outlineVariant = Neutral85,
)

@Composable
fun Swifty_CompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
