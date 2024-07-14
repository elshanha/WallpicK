package com.elshan.wallpick.presentation.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesomeMotion
import androidx.compose.material.icons.rounded.DownloadDone
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.R
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.components.CustomTopAppBar
import com.elshan.wallpick.presentation.screen.home.components.WallpaperSection
import com.elshan.wallpick.presentation.screen.login.sign_in.GoogleAuthUiClient
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.CircleProfileImage
import com.elshan.wallpick.utils.topBarColors


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllWallpapersScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
) {

//    LaunchedEffect(mainUiState.allWallpapers.isEmpty()) {
//        if (mainUiState.allWallpapers.isEmpty()) {
//            onEvent(MainUiEvents.GetFromFirebase)
//        }
//    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        topBar = {
            CustomTopAppBar(
                scrollBehavior = scrollBehavior,
                navController = navController,
                title = "All",
            )
        }
    ) { paddingValues ->
        val paddingValues = paddingValues

//        WallpaperList(
//            mainUiState,
//            navController,
//            onEvent,
//            scrollBehavior,
//            paddingValues,
         //   wallpaperList = mainUiState.allWallpapers
    //    )
    }
}