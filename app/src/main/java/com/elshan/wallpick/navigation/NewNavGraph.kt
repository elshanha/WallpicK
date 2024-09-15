package com.elshan.wallpick.navigation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.elshan.wallpick.MainActivity
import com.elshan.wallpick.main.domain.model.Wallpaper
import com.elshan.wallpick.presentation.screen.search.SearchScreen
import com.elshan.wallpick.presentation.screen.category.CategorizedWallpapers
import com.elshan.wallpick.presentation.screen.details.DetailsScreen
import com.elshan.wallpick.presentation.screen.details.FullScreenImage
import com.elshan.wallpick.presentation.screen.home.AllWallpapersScreen
import com.elshan.wallpick.presentation.screen.login.Register
import com.elshan.wallpick.presentation.screen.login.profile.ProfileScreen
import com.elshan.wallpick.presentation.screen.login.sign_in.SignInScreen
import com.elshan.wallpick.presentation.screen.main.MainScreen
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.screen.popular.MostDownloadedWallpapersScreen
import com.elshan.wallpick.presentation.screen.popular.PopularWallpapersScreen
import com.elshan.wallpick.presentation.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun NewNavGraph(
    context: MainActivity,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val mainUiStateViewModel by mainViewModel.uiState.collectAsStateWithLifecycle()
    val googleAuthUiClient = mainViewModel.googleAuthUiClient

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (googleAuthUiClient.getSignedInUser() != null) Screen.Main else Screen.Login
    ) {

        composable<Screen.Login> {
            LaunchedEffect(key1 = Unit) {
                if (googleAuthUiClient.getSignedInUser() != null) {
                    navController.navigate(Screen.Main)
                }
            }

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        context.lifecycleScope.launch {
                            val signInResult =
                                googleAuthUiClient.signInWithIntent(
                                    intent = result.data
                                        ?: return@launch
                                )
                            mainViewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )


            LaunchedEffect(key1 = mainUiStateViewModel.isSignInSuccessful) {
                if (mainUiStateViewModel.isSignInSuccessful) {
                    onEvent(MainUiEvents.ShowNotification("Welcome back ${googleAuthUiClient.getSignedInUser()?.firebaseUser?.displayName}"))
                    navController.navigate(Screen.Main) {
                        popUpTo(Screen.Login) { inclusive = true }
                    }
                    onEvent(MainUiEvents.ResetState)
                }
            }

            SignInScreen(
                mainUiState = mainUiStateViewModel,
                onEvent = onEvent,
                navController = navController,
                onSignInClick = {
                    context.lifecycleScope.launch {
                        val signInIntentSender =
                            googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
        composable<Screen.Profile>() {
            val profile = it.toRoute<Screen.Profile>()
            ProfileScreen(
                userData = googleAuthUiClient.getSignedInUser(),
                mainUiState = mainUiStateViewModel,
                onEvent = onEvent,
                navController = navController
            )
        }
        composable<Screen.Register> {
            Register()
        }
        composable<Screen.Main>() {

            MainScreen(
                navController = navController,
                mainUiState = mainUiStateViewModel,
                onEvent = onEvent,
            )

        }

        composable<Screen.WallpaperDetails>(
            enterTransition = {
                fadeIn(
                    animationSpec = spring(
                        dampingRatio = 0.8f,
                        stiffness = 400f
                    ),
                    initialAlpha = 5f
                )
            }
        ) {
            val details = it.toRoute<Screen.WallpaperDetails>()
            DetailsScreen(
                wallpaper = Wallpaper(
                    category = details.category,
                    id = details.id,
                    imageUrl = details.imageUrl,
                    name = details.name,
                    resolution = details.resolution,
                    tags = details.tags
                ),
                onEvent = onEvent,
                navController = navController,
                mainUiState = mainUiStateViewModel
            )
        }

        composable<Screen.WallpaperByCategory> {
            val category = it.toRoute<Screen.WallpaperByCategory>()
            CategorizedWallpapers(
                navController = navController,
                mainUiState = mainUiStateViewModel,
                onEvent = onEvent,
                category = category.category
            )
        }

        composable<Screen.FullScreenImage>() {
            val fullScreen = it.toRoute<Screen.FullScreenImage>()
            FullScreenImage(
                wallpaper = Wallpaper(
                    name = fullScreen.name,
                    imageUrl = fullScreen.imageUrl,
                    resolution = fullScreen.resolution,
                    category = fullScreen.category,
                    id = fullScreen.id
                ),
                onEvent = onEvent
            )

        }

        composable<Screen.Search>() {
            SearchScreen(
                mainUiState = mainUiStateViewModel,
                onEvent = onEvent,
                navController = navController
            )
        }

        composable<Screen.AllWallpapers> {
            AllWallpapersScreen(
                navController = navController,
                mainUiState = mainUiState,
                onEvent = onEvent,
            )
        }

        composable<Screen.MostDownloadedWallpapers> {
            MostDownloadedWallpapersScreen(
                mainUiState = mainUiState,
                navController = navController,
                onEvent = onEvent,
            )
        }

        composable<Screen.PopularWallpapers> {
            PopularWallpapersScreen(
                mainUiState = mainUiState,
                navController = navController,
                onEvent = onEvent,
            )
        }
    }
}
