package com.rohaniyat.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Emerald,
    onPrimary = CardLight,
    secondary = Gold,
    background = Cream,
    surface = CardLight,
    onBackground = Ink,
    onSurface = Ink,
    error = Danger
)

private val DarkColors = darkColorScheme(
    primary = Gold,
    onPrimary = Ink,
    secondary = EmeraldLight,
    background = BgDark,
    surface = CardDark,
    onBackground = InkDarkText,
    onSurface = InkDarkText,
    error = Danger
)

@Composable
fun RohaniyatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
