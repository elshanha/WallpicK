package com.elshan.wallpick.utils.custom


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun CustomSnackbar(
    snackbarData: SnackbarData,
) {

    val brush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.primary,
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = MaterialTheme.shapes.small
            )
            .background(
                brush,
            )
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = snackbarData.visuals.message,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.performAction() }) {
                    Text(
                        text = actionLabel,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun CustomSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
        hostState = hostState,
        snackbar = { snackbarData -> CustomSnackbar(snackbarData) }
    )
}
