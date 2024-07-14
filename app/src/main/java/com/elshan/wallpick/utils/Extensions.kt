package com.elshan.wallpick.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.elshan.wallpick.presentation.screen.main.MainUiState

@Composable
fun iconButtonColors(): IconButtonColors {
    return IconButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = Color.White
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun topBarColors(): TopAppBarColors {
    return TopAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.primary,
        navigationIconContentColor = MaterialTheme.colorScheme.primary,
        actionIconContentColor = MaterialTheme.colorScheme.primary,
        scrolledContainerColor = MaterialTheme.colorScheme.background,
    )
}

sealed class BaseColor {
    data object Muted : BaseColor()
    data object Vibrant : BaseColor()
    data object ThemeMuted : BaseColor()
    data object ThemeVibrant : BaseColor()
    data object Dominant : BaseColor()
    data object BodyTextColor : BaseColor()
    data object TitleTextColor : BaseColor()
}

@Composable
fun autoThemedPaletteColor(
    mainUiState: MainUiState,
    color: BaseColor,
): Color {
    val isDarkTheme = isSystemInDarkTheme()
    return when (color) {
        BaseColor.Dominant -> mainUiState.dominantColor
        BaseColor.Muted -> mainUiState.mutedColor
        BaseColor.Vibrant -> mainUiState.vibrantColor
        BaseColor.ThemeMuted -> if (isDarkTheme) mainUiState.darkMutedColor else mainUiState.lightMutedColor
        BaseColor.ThemeVibrant -> if (isDarkTheme) mainUiState.darkVibrantColor else mainUiState.lightVibrantColor
        BaseColor.BodyTextColor -> mainUiState.bodyTextColor
        BaseColor.TitleTextColor -> mainUiState.titleTextColor
    }
}


















