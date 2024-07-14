package com.elshan.wallpick.presentation.screen.login.sign_in

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.elshan.wallpick.R
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.viewmodel.MainViewModel

@Composable
fun SignInScreen(
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    navController: NavController,
    onSignInClick: () -> Unit,
) {

//    LaunchedEffect(key1 = mainUiState.allWallpapers.isEmpty()) {
//        onEvent(MainUiEvents.GetFromFirebase)
//    }

    val context = LocalContext.current
    LaunchedEffect(key1 = mainUiState.signInError) {
        mainUiState.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.aspectRatio(3f))
        Text(
            text = "Excited to see you again!\nPlease sign in to continue...",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = MaterialTheme.typography.bodyLarge.fontWeight,
                fontStyle = FontStyle.Italic,

                textMotion = TextMotion.Animated,

                )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            modifier = Modifier
                .fillMaxSize(0.7f),
            painter = painterResource(id = R.drawable.login_illustrator),
            contentDescription = "login"
        )

        Spacer(modifier = Modifier.height(20.dp))
        MyButton(

            onClick = onSignInClick,
            text = "Sign in with Google",
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            icon = painterResource(id = R.drawable.google)
        )
    }
}

