package com.elshan.wallpick.presentation.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.presentation.screen.home.WallpaperItem
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.screen.search.components.ColorSearchBar
import com.elshan.wallpick.presentation.screen.search.components.SearchField
import com.elshan.wallpick.presentation.screen.search.components.TagSearchBar
import com.elshan.wallpick.ui.theme.Purple80
import com.elshan.wallpick.utils.animation.AnimatedScrollText
import com.elshan.wallpick.utils.animation.AnimatedText
import com.elshan.wallpick.utils.topWindowInsetsPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
) {
    var query by rememberSaveable { mutableStateOf(mainUiState.query) }
    var showColorSearch by remember { mutableStateOf(true) }
    var showTagSearch by remember { mutableStateOf(true) }


    LaunchedEffect(mainUiState.query) {
        query = mainUiState.query
    }

    Column(
        modifier = Modifier
    ) {
        SearchField(
            query = query,
            onEvent = onEvent,
            onSearch = { newQuery ->
                query = newQuery
                onEvent(MainUiEvents.SearchWallpapers(newQuery))
            },
            onClear = {
                query = ""
                onEvent(MainUiEvents.SearchWallpapers(""))
                showColorSearch = true
                showTagSearch = true
            }
        )
        AnimatedVisibility(
            visible = showColorSearch && mainUiState.searchWallpapers.isEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            ColorSearchBar(onColorSelected = { color ->
                query = colors[color] ?: ""
                onEvent(MainUiEvents.ColorSelected(color))
                showColorSearch = false
            })
        }

        AnimatedVisibility(
            visible = showTagSearch && mainUiState.searchWallpapers.isEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            TagSearchBar(onTagSelected = { tag ->
                onEvent(MainUiEvents.SearchWallpapers(tag))
                showTagSearch = false
            }, tags = mainUiState.tagsList.ifEmpty { tagList })
        }

        if (mainUiState.searchWallpapers.isNotEmpty()) {

            LazyVerticalGrid(
                state = rememberLazyGridState(),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = topWindowInsetsPadding(),
                        start = 16.dp,
                        end = 16.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(mainUiState.searchWallpapers) { wallpaper ->
                    WallpaperItem(
                        wallpaper = wallpaper,
                        navController = navController,
                        mainUiState = mainUiState,
                        onEvent = onEvent,
                    )
                }
            }
        } else {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
            }
        }
    }
}


@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen(
        navController = NavController(LocalContext.current),
        mainUiState = MainUiState(),
        onEvent = {}
    )
}

val colors = mapOf(
    Color(0xFFF44336) to "Red",
    Color(0xFF4CAF50) to "Green",
    Color(0xFF2196F3) to "Blue",
    Color(0xFFFFEB3B) to "Yellow",
    Color(0xFF9C27B0) to "Purple",
    Color(0xFF00BCD4) to "Cyan",
    Color(0xFF808080) to "Gray",
    Color(0xFFFFFFFF) to "White"
)



val tagList = listOf(
    "girl", "Ana De Armas", "purple", "yellow", "dark", "anime", "animal"
)


