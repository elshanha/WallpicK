package com.elshan.wallpick.utils.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun FilterContent(selectedFilter: FilterType, onFilterSelected: (FilterType) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        FilterOption(
            text = "Latest",
            selected = selectedFilter == FilterType.LATEST,
            onClick = { onFilterSelected(FilterType.LATEST) }
        )
        FilterOption(
            text = "Popular",
            selected = selectedFilter == FilterType.POPULAR,
            onClick = { onFilterSelected(FilterType.POPULAR) }
        )
        FilterOption(
            text = "Most Downloaded",
            selected = selectedFilter == FilterType.MOST_DOWNLOADED,
            onClick = { onFilterSelected(FilterType.MOST_DOWNLOADED) }
        )
    }
}

@Composable
fun FilterOption(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = text, style = MaterialTheme.typography.bodyLarge)
    }
}


enum class FilterType {
    LATEST,
    POPULAR,
    MOST_DOWNLOADED
}


