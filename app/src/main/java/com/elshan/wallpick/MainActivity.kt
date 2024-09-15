package com.elshan.wallpick

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elshan.wallpick.navigation.NewNavGraph
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.onboarding.OnboardingScreen
import com.elshan.wallpick.presentation.viewmodel.MainViewModel
import com.elshan.wallpick.ui.theme.WallpicKTheme
import com.elshan.wallpick.utils.custom.CustomSnackbarHost
import com.elshan.wallpick.utils.custom.FilterType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        splashScreen.apply {
            setKeepOnScreenCondition {
                !mainViewModel.uiState.value.isReady
            }
        }
        setContent {
            val mainUiState by mainViewModel.uiState.collectAsStateWithLifecycle()
            WallpicKTheme(
                dynamicColor = false,
            ) {

                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(Unit) {
                    mainViewModel.setSnackbarHostState(snackbarHostState)
                    mainViewModel.extractColorsForNewWallpapers()
                }
                Scaffold(
                    snackbarHost = {
                        CustomSnackbarHost(
                            hostState = snackbarHostState,
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->

                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    val isSystemInDarkMode = isSystemInDarkTheme()
                    val systemUiColor = rememberSystemUiController()
                    SideEffect {
                        systemUiColor.setSystemBarsColor(
                            color = Color.Transparent,
                            darkIcons = !isSystemInDarkMode
                        )
                    }
                    val padding = innerPadding

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionState =
                            rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                        if (!permissionState.status.isGranted) {
                            LaunchedEffect(key1 = Unit) {
                                permissionState.launchPermissionRequest()
                                mainViewModel.createNotificationChannel()
                            }
                        } else {
                            Unit
                        }
                    }

                    Box(
                        Modifier
                            .statusBarsPadding()
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        if (mainUiState.isReady) {
                            if (mainUiState.onboardingCompleted) {
                                NewNavGraph(
                                    context = this@MainActivity,
                                    mainUiState = mainUiState,
                                    onEvent = mainViewModel::onEvent,
                                    mainViewModel = mainViewModel,
                                )
                            } else {
                                OnboardingScreen(onFinish = {
                                    mainViewModel.onEvent(MainUiEvents.CompleteOnboarding)
                                })
                            }
                        }
                    }
                }
            }
        }
    }
}

