package com.elshan.wallpick.presentation.screen.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class ScrollStateHandler {
    var bottomBarVisible by mutableStateOf(true)
    private var lastOffset by mutableIntStateOf(0)

    fun onScroll(offset: Int) {
        if (offset > lastOffset) {
            bottomBarVisible = false
        } else if (offset < lastOffset) {
            bottomBarVisible = true
        }
        lastOffset = offset
    }
}

