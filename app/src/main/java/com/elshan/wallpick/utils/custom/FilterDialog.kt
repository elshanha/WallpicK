package com.elshan.wallpick.utils.custom

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun FilterDialog(
    onDismissRequest: () -> Unit,
    selectedFilter: FilterType,
    onFilterSelected: (FilterType) -> Unit
) {
    AlertDialog(
        modifier = Modifier.padding(horizontal = 32.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.primary,
        textContentColor = MaterialTheme.colorScheme.primary,
        iconContentColor = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.extraLarge,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
        },
        title = {
            Text(
                text = "Filter Options",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            FilterContent(selectedFilter = selectedFilter, onFilterSelected = onFilterSelected)
        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )
    )
}

