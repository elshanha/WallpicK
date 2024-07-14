package com.elshan.wallpick.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.R

@Composable
fun DefaultLoadingContent(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.photo),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.medium
            )
            .aspectRatio(3f / 4f)
    )
}