package com.elshan.wallpick.utils.animation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color


@Composable
fun FavoriteHeartButton(
    color: Color = ButtonDefaults.filledTonalButtonColors().containerColor,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
) {

    val icon = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    val animatedColor by animateColorAsState(
        targetValue = if (isFavorite) MaterialTheme.colorScheme.error else LocalContentColor.current,
        animationSpec = tween(durationMillis = 500), label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (isFavorite) 1.2f else 1.0f,
        animationSpec = spring(stiffness = Spring.DampingRatioLowBouncy),
        label = "" // Customize spring behavior
    )

    FilledTonalIconButton(
        onClick = onFavoriteToggle,
        modifier = Modifier,
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = color
        )
    ) {
        Icon(
            imageVector = icon, // Replace with your heart icon resource
            tint = animatedColor,
            contentDescription = "Favorite Button",
            modifier = Modifier.scale(scale)
        )
    }
}