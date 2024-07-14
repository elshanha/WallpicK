package com.elshan.wallpick.presentation.screen.favorite.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.R
import kotlinx.coroutines.delay


@Composable
fun AnimatedEmptyFavorites() {
    var scale by remember { mutableFloatStateOf(1f) }
    var repeatCount by remember { mutableIntStateOf(0) }
    val maxRepeats = 2
    LaunchedEffect(Unit) {
        while (repeatCount < maxRepeats) {
            scale = 1.5f
            delay(500)
            scale = 1f
            delay(500)
            repeatCount++
        }
    }

    val animatedScale by animateFloatAsState(targetValue = scale, label = "")

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = if (repeatCount == 2) "No Favorites" else "Checking Favorites")
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = if (repeatCount == 2) painterResource(id = R.drawable.dark_broken_heart)
                else painterResource(id = R.drawable.heart),
                contentDescription = "No Favorites",
                modifier = Modifier
                    .size(128.dp)
                    .scale(animatedScale)
            )
        }
    }
}
