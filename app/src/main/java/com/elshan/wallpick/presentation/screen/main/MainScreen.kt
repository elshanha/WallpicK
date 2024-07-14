package com.elshan.wallpick.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.elshan.wallpick.navigation.BottomBarScreens
import com.elshan.wallpick.navigation.BottomNavigationBar
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.time.debounce
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun MainScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit,
) {


    val bottomBarNavController = rememberNavController()
    val listScrollState = rememberLazyListState()
    val gridScrollState = rememberLazyGridState()
    val scrollStateHandler = remember { ScrollStateHandler() }

    LaunchedEffect(listScrollState) {
        snapshotFlow { listScrollState.firstVisibleItemScrollOffset }
            .collect { offset -> scrollStateHandler.onScroll(offset) }
    }

    LaunchedEffect(gridScrollState) {
        snapshotFlow { gridScrollState.firstVisibleItemScrollOffset }
            .collect { offset -> scrollStateHandler.onScroll(offset) }
    }

    Scaffold(

        content = { paddingValues ->
            BottomBarScreens(
                modifier = Modifier
                    .padding(
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
                onEvent = onEvent,
                listScrollState = listScrollState,
                gridScrollState = gridScrollState
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                AnimatedVisibility(
                    visible = scrollStateHandler.bottomBarVisible,
                    enter = fadeIn(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = fadeOut(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ) + slideOutVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    BottomNavigationBar(
                        bottomBarNavController = bottomBarNavController,
                        navController = navController,
                    )
                }
            }

        },
    )
}


//@Preview
//@Composable
//fun MainScreenPreview() {
//    MainScreen(
//        navController = rememberNavController(),
//        mainUiState = MainUiState(),
//        onEvent = {},
//    )
//}
