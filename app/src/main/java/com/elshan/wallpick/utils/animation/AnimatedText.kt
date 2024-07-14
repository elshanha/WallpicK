package com.elshan.wallpick.utils.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun AnimatedText(
    firstText: String,
    secondText: String,
    firstFontSize: Float = 16f,
    secondFontSize: Float = 18f,
) {
    var transitionState by remember { mutableStateOf(TransitionState.FirstText) }

    // Define the animations
    val scaleAnim = rememberInfiniteTransition(label = "")
    val scale by scaleAnim.animateFloat(
        label = "",
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val colorAnim = rememberInfiniteTransition(label = "")
    val textColor by colorAnim.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.outline,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val scaleSecondText by animateFloatAsState(
        targetValue = if (transitionState == TransitionState.SecondText) 1.5f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ), label = ""
    )

    // Start animation sequence
    LaunchedEffect(Unit) {
        delay(1000) // Initial delay before transitioning to second text
        transitionState = TransitionState.SecondText
        delay(2000) // Duration for second text animation
        transitionState = TransitionState.Stopped
    }

    // Display the animated text
    val (text, fontSize) = when (transitionState) {
        TransitionState.FirstText -> Pair(firstText, firstFontSize)
        TransitionState.SecondText -> Pair(secondText, secondFontSize * scaleSecondText)
        TransitionState.Stopped -> Pair(secondText, secondFontSize)
    }

    val finalColor = if (transitionState == TransitionState.FirstText) textColor else MaterialTheme.colorScheme.outline

    Text(
        text = text,
        fontSize = fontSize.sp,
        color = finalColor,
        letterSpacing = 0.15.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(16.dp)
    )
}

enum class TransitionState {
    FirstText,
    SecondText,
    Stopped
}


