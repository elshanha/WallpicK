package com.elshan.wallpick.presentation.screen.login.profile

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.elshan.wallpick.main.domain.model.User
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.notifications.NotificationsService
import com.elshan.wallpick.presentation.screen.login.sign_in.AuthenticatedUser
import com.elshan.wallpick.presentation.screen.login.sign_in.MyButton
import com.elshan.wallpick.presentation.screen.login.sign_in.UserData
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.viewmodel.MainViewModel

@Composable
fun ProfileScreen(
    userData: AuthenticatedUser?,
    modifier: Modifier = Modifier,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    navController: NavController
) {

    val fbUser = userData?.firebaseUser
    val context = LocalContext.current

    LaunchedEffect(key1 = fbUser != null) {
        if (fbUser != null)
            onEvent(MainUiEvents.GetUserInfo(fbUser.uid))
    }

    Column(
        modifier = modifier

            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.22f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Box(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxHeight(0.66f)
                        .fillMaxWidth()
                )
            }
            Box(
                modifier = modifier
                    .fillMaxHeight()
            ) {
                if (fbUser?.photoUrl != null) {
                    Box(
                        modifier = modifier
                            .align(Alignment.BottomCenter)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    ) {
                        AsyncImage(
                            model = fbUser.photoUrl,
                            contentDescription = "Profile picture",
                            modifier = Modifier
                                .padding(5.dp)
                                .size(128.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(vertical = 24.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            fbUser?.displayName?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(bottom = 24.dp),
            text = fbUser?.email ?: "",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall,
        )


        var notificationsEnabled by remember { mutableStateOf(mainUiState.notificationsEnabled) }

//        SettingsItem(
//            text = if (biometricEnabled) "Biometric Enabled" else "Biometric Disabled",
//            enabled = biometricEnabled,
//            onCheckedChange = {
//                biometricEnabled = it
//             //   mainViewModel.updateBiometricsEnabled(it)
//            }
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//        Text(text = "Notifications", modifier.padding(start = 16.dp), style = MaterialTheme.typography.titleLarge)
//        Spacer(modifier = Modifier.height(24.dp))
//
        SettingsItem(
            text = "Notifications",
            enabled = notificationsEnabled,
            onCheckedChange = {
                notificationsEnabled = it
                onEvent.apply {
                    this(MainUiEvents.SaveNotificationPreference(it))
                    this(MainUiEvents.ToggleNotification(it))
                }

            })

       Spacer(modifier = Modifier.weight(1f))

        MyButton(
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.CenterHorizontally),
            onClick = {
                onEvent(MainUiEvents.SignOut)
                onEvent(MainUiEvents.ShowSnackBar(message = "Signed Out"))
                navController.navigate(Screen.Login) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            text = "Sign Out",
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
        )

    }
}

@Composable
private fun ProfileInfoSection(
    info: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier

                .padding(start = 16.dp),
            text = info,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .padding(end = 16.dp),
            text = value,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodySmall,
        )

    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean,
    onCheckedChange: ((Boolean) -> Unit)
) {

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(start = 20.dp),
            style = MaterialTheme.typography.bodyLarge,
            text = text
        )

        Switch(
            modifier = Modifier
                .padding(end = 20.dp),
            checked = enabled, onCheckedChange = onCheckedChange
        )
    }

}