package com.elshan.wallpick.presentation.screen.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.elshan.wallpick.R
import com.elshan.wallpick.ui.theme.WallpicKTheme

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxHeight()
        ) {
            val colors = Brush.linearGradient(
                listOf(Color.Blue.copy(0.9f), Color.White)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(colors)
            ) {

                Image(
                    modifier = Modifier
                        .padding(top = 48.dp, start = 48.dp, end = 48.dp)
                        .aspectRatio(0.8f),
                    painter = painterResource(id = R.drawable.wallpaper_app),
                    contentDescription = null
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .background(Color.Transparent)
                    .fillMaxHeight(0.45f)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier
                            .padding(top = 38.dp, start = 24.dp, end = 24.dp),
                        text = "Welcome to WallpicK!",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    )
                    Text(
                        modifier = Modifier
                            //  .fillMaxWidth()
                            .padding(24.dp),
                        text = "WallpicK is a wallpaper app and you can get your favorite wallpaper from here! Enjoy!",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Light,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ),
                    )
                    Button(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxHeight(0.26f),
                        colors = ButtonDefaults.buttonColors().copy(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ),
                        onClick = {
                            println("clicked")

                            onFinish()

                        }
                    ) {
                        Text(text = "Explore", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    WallpicKTheme {
        OnboardingScreen {}
    }
}
