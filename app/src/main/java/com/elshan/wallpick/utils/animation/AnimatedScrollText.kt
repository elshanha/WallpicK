package com.elshan.wallpick.utils.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay


@Composable
fun AnimatedScrollText() {
    val messages = listOf("tags", "colors", "categories", "names")
    val messageColors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onSecondary,
        MaterialTheme.colorScheme.outline,
        MaterialTheme.colorScheme.onSecondaryContainer,
    )
    var currentMessageIndex by remember { mutableIntStateOf(0) }
    val currentMessage = messages[currentMessageIndex]
    var repeatCount by remember { mutableIntStateOf(0) }
    val currentColor = messageColors[currentMessageIndex]

    LaunchedEffect(Unit) {
        while (repeatCount < messages.size - 1) {
            delay(1500)
            currentMessageIndex = (currentMessageIndex + 1) % messages.size
            repeatCount++
        }
    }

    Row {
        Text(
            text = "Search for ",
            style = MaterialTheme.typography.labelMedium
        )
        AnimatedContent(
            targetState = currentMessage,
            transitionSpec = {
                slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            }, label = "currentMessageLabel"
        ) { targetMessage ->
            Text(
                text = targetMessage,
                style = MaterialTheme.typography.labelMedium,
                color = currentColor
            )
        }
    }

}

