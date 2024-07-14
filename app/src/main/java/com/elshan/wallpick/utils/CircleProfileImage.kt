package com.elshan.wallpick.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elshan.wallpick.presentation.screen.login.sign_in.GoogleAuthUiClient
import com.elshan.wallpick.presentation.screen.login.sign_in.UserData
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.viewmodel.MainViewModel


@Composable
fun CircleProfileImage(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val userData = viewModel.googleAuthUiClient.getSignedInUser()?.firebaseUser
    if (userData?.photoUrl != null) {
        Box {
            AsyncImage(
                model = userData.photoUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .clickable {
                        navController.navigate(
                            Screen.Profile(
                                title = userData.displayName ?: "username",
                                description = "This is my description"
                            )
                        )
                    }
                    .padding(end = 16.dp, start = 16.dp)
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
}