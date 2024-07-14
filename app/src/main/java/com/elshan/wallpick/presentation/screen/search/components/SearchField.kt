package com.elshan.wallpick.presentation.screen.search.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.utils.animation.AnimatedScrollText


@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    query: String,
    onClear: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    onEvent: (MainUiEvents) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    if (isFocused) {
        animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500), label = ""
        ).value
    }

    OutlinedTextField(
        modifier = modifier
            .onFocusEvent {
                isFocused = it.isFocused
            }
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        placeholder = {
           AnimatedScrollText()
        },
        leadingIcon = {
            IconButton(onClick = {
                onSearch(query)
            }) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    onClear()
                }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            }
        },
        value = query,
        onValueChange = {
            onSearch(it)
        }
    )
}