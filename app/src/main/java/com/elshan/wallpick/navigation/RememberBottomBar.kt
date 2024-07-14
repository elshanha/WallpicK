package com.elshan.wallpick.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow

@Composable
fun rememberBottomBarVisibilityForList(scrollState: LazyListState): State<Boolean> {
    val isScrollingUp = remember { mutableStateOf(true) }

    LaunchedEffect(scrollState) {
        var previousIndex = scrollState.firstVisibleItemIndex
        var previousOffset = scrollState.firstVisibleItemScrollOffset

        snapshotFlow { scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                isScrollingUp.value = if (index == previousIndex) {
                    offset < previousOffset
                } else {
                    index < previousIndex
                }
                previousIndex = index
                previousOffset = offset
            }
    }

    return isScrollingUp
}

@Composable
fun rememberBottomBarVisibilityForGrid(scrollState: LazyGridState): State<Boolean> {
    val isScrollingUp = remember { mutableStateOf(true) }

    LaunchedEffect(scrollState) {
        var previousIndex = scrollState.firstVisibleItemIndex
        var previousOffset = scrollState.firstVisibleItemScrollOffset

        snapshotFlow { scrollState.firstVisibleItemIndex to scrollState.firstVisibleItemScrollOffset }
            .collect { (index, offset) ->
                isScrollingUp.value = if (index == previousIndex) {
                    offset < previousOffset
                } else {
                    index < previousIndex
                }
                previousIndex = index
                previousOffset = offset
            }
    }

    return isScrollingUp
}
