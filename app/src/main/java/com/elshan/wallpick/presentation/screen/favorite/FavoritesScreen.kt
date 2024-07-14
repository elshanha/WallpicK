package com.elshan.wallpick.presentation.screen.favorite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.R
import com.elshan.wallpick.presentation.screen.details.component.InfoDialog
import com.elshan.wallpick.presentation.screen.favorite.component.AnimatedEmptyFavorites
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.presentation.screen.search.components.SearchField
import com.elshan.wallpick.utils.topBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    navController: NavController,
    gridScrollState: LazyGridState
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var isSearchActive by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf(mainUiState.favoritesQuery) }


    if (showDialog && mainUiState.favoriteWallpapers.isNotEmpty()) {
        InfoDialog(
            onConfirm = {
                onEvent(MainUiEvents.DeleteAllFavorites)
                onEvent(
                    MainUiEvents.ShowSnackBar(
                        message = "All favorites deleted",
                        isBottomAppBarVisible = true
                    )
                )
                showDialog = false
            },
            title = "Delete All",
            message = "Are you sure you want to delete all wallpapers?",
            onDismissRequest = { showDialog = false }
        )
    }

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    scrollBehavior = scrollBehavior,
                    colors = topBarColors(),
                    title = {
                        Text(
                            text = "Favorites",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            isSearchActive = !isSearchActive
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search_square),
                                contentDescription = "Search"
                            )
                        }
                        IconButton(
                            onClick = {
                                if (mainUiState.favoriteWallpapers.isEmpty()) {
                                    onEvent(
                                        MainUiEvents.ShowSnackBar(
                                            message = "No favorites",
                                        )
                                    )
                                }
                                showDialog = true
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                )

                AnimatedVisibility(
                    visible = isSearchActive,
                    enter = expandVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)),
                    exit = shrinkVertically(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                ) {
                    SearchField(
                        modifier = Modifier
                            .padding(bottom = 12.dp),
                        query = query,
                        onSearch = { search ->
                            query = search
                            onEvent(MainUiEvents.SearchFavorites(search))
                        },
                        onClear = {
                            query = ""
                            onEvent(MainUiEvents.SearchFavorites(""))
                        },
                        onEvent = onEvent
                    )
                }
            }
        }
    ) {


        val paddingValues = it

        if (mainUiState.favoriteWallpapers.isNotEmpty()) {
            LazyVerticalGrid(
                state = gridScrollState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        start = 16.dp,
                        end = 16.dp
                    ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(mainUiState.filteredFavorites) { wallpaper ->
                    if (wallpaper.isFavorite) {
                        FavoriteImageItem(
                            wallpaperEntity = wallpaper,
                            navController = navController,
                            onEvent = onEvent
                        )
                    }
                }
            }
        } else {
            AnimatedEmptyFavorites()
        }
    }
}
