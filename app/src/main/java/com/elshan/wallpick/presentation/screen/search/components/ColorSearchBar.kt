package com.elshan.wallpick.presentation.screen.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.presentation.screen.search.colors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ColorSearchBar(onColorSelected: (Color) -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            delay(200)
            isVisible = true
        }
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            animationSpec = spring(
                dampingRatio = 0.8f,
                stiffness = 100f
            ),
            initialOffsetX = { 1000 }
        )
    ) {
        Column {
            Text(
                text = "Search by color",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(colors.keys.toList()) { color ->
                    Box(
                        modifier = Modifier
                            .background(color = color, shape = MaterialTheme.shapes.medium)
                            .size(48.dp)
                            .clickable {
                                onColorSelected(color)
                            }
                    )
                }
            }
        }
    }
}
