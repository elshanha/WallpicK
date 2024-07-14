package com.elshan.wallpick.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class BottomScreen {
    @Serializable
    data object Home : BottomScreen()
    @Serializable
    data object Category : BottomScreen()
    @Serializable
    data object Favorite : BottomScreen()
}