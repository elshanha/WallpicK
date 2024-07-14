package com.elshan.wallpick.presentation.screen.category

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.presentation.screen.category.components.CategoryItem
import com.elshan.wallpick.presentation.screen.main.MainUiEvents
import com.elshan.wallpick.presentation.screen.main.MainUiState
import com.elshan.wallpick.utils.topBarColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
    gridScrollState: LazyGridState,
    listScrollState: LazyListState,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                colors = topBarColors(),
                title = {
                    Text(
                        text = "Categories",
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.padding(end = 16.dp),
                        onClick = {
                            onEvent(MainUiEvents.SaveLayoutPreference(!mainUiState.isGridLayout))
                        }) {
                        Icon(
                            imageVector = if (mainUiState.isGridLayout) Icons.AutoMirrored.Filled.ViewList else Icons.Default.GridView,
                            contentDescription = ""
                            )
                    }
                }
            )
        }
    ) {
        val paddingValues = it

        AnimatedContent(
            targetState = mainUiState.isGridLayout,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) + scaleIn(
                    initialScale = 0.9f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                ) togetherWith
                        fadeOut(animationSpec = tween(300)) + scaleOut(
                    targetScale = 1.1f,
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )

            }, label = ""
        ) { isGridLayout ->
            if (!isGridLayout) {
                LazyColumn(
                    state = listScrollState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                        .padding(
                            top = paddingValues.calculateTopPadding(),
                            start = 16.dp,
                            end = 16.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(mainUiState.categories) { category ->
                        if (mainUiState.categories.isNotEmpty()) {

                            CategoryItem(
                                category = category,
                                navController = navController,
                                onEvent = onEvent
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.primary)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Text(text = "No category found")
                            }
                        }
                    }
                }
            } else {
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(mainUiState.categories) { category ->
                        CategoryItem(
                            category = category,
                            navController = navController,
                            onEvent = onEvent,
                            isGridView = true,
                        )
                    }
                }
            }
        }
    }
}



