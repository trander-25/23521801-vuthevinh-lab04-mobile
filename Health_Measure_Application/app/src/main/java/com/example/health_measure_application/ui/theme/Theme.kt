package com.example.health_measure_application.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = WearAccentOrange,
    secondary = WearAccentOrange,
    background = WearDarkBackground,
    surface = WearDarkSurface,
    onPrimary = WearDarkBackground,
    onSecondary = WearDarkBackground,
    onBackground = WearOnDark,
    onSurface = WearOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = WearAccentOrange,
    secondary = WearAccentOrange,
    background = WearDarkBackground,
    surface = WearDarkSurface,
    onPrimary = WearDarkBackground,
    onSecondary = WearDarkBackground,
    onBackground = WearOnDark,
    onSurface = WearOnDark
)

@Composable
fun Health_Measure_ApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}