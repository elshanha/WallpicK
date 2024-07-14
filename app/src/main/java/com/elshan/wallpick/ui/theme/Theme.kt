package com.elshan.wallpick.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFA3D398),
    onPrimary = Color(0xFF0D380D),
    secondaryContainer = Color(0xFF3C4B38),
    onSecondaryContainer = Color(0xFFD6E8CE),
    background = Color(0xFF10140F),
    secondary = Color(0xFFE0E4DA),
    onSecondary = Color(0xFFE0E4DA).copy(alpha = .6f),
    outline = Color(0xFF8C9388),
    surface = Color(0xFF272B25),
    error = Color(0xFFB00020),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3D6838),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E8CE),
    onSecondaryContainer = Color(0xFF111F0F),
    background = Color(0xFFF7FBF1),
    secondary = Color(0xFF191D17),
    onSecondary = Color(0xFF191D17).copy(alpha = .6f),
    outline = Color(0xFF73796F),
    surface = Color(0xFFE6E9E0),
    error = Color(0xFFB00020),
)

@Composable
fun WallpicKTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = CustomShapes
    )
}