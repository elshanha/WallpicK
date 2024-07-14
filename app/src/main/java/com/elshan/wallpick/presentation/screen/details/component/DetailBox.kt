package com.elshan.wallpick.presentation.screen.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Category
import androidx.compose.material.icons.sharp.CropLandscape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.main.domain.model.Wallpaper


@Composable
fun DetailBox(
    modifier: Modifier = Modifier,
    wallpaper: Wallpaper,
    textColour: Color = LocalTextStyle.current.color,
    color: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Box(
        modifier = modifier
            .padding(16.dp)
    ) {
        Column(
            modifier = modifier
                .background(
                    color = color,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(16.dp)
                .border(
                    width = 2.dp,
                    color = color,
                    shape = MaterialTheme.shapes.extraLarge
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ) {
                TextBox(
                    imageVector = Icons.Sharp.Category,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        color = textColour
                    ),
                    text = wallpaper.category.replaceFirstChar { it.uppercase() }
                )
                TextBox(
                    imageVector = Icons.Sharp.CropLandscape,
                    text = wallpaper.resolution,
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        color = textColour
                    )
                )
            }
        }
    }
}

