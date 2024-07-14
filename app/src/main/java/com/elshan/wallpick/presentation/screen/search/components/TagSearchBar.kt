package com.elshan.wallpick.presentation.screen.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TagSearchBar(
    onTagSelected: (String) -> Unit, tags: List<String>
) {
    var isVisible by remember {
        mutableStateOf(false)
    }
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
            initialOffsetX = { 1000 },
            animationSpec = spring(
                dampingRatio = 0.8f,
                stiffness = 100f
            )
        )
    ) {
        Column {
            Text(
                text = "Search by tags",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(tags) { tag ->

                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = MaterialTheme.shapes.extraLarge
                            )
                            .clickable {
                                onTagSelected(tag)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = tag, color = Color.White)
                    }
                }
            }
        }
    }
}
