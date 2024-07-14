package com.elshan.wallpick.presentation.screen.details.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun InfoDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.primary,
        textContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        iconContentColor = MaterialTheme.colorScheme.primary,
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            Button(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContentColor = MaterialTheme.colorScheme.primary
                ),
                onClick = {
                onConfirm()
            }) {
                Text(text = "Ok")
            }
        },
        dismissButton = {
            FilledTonalButton(
                colors = ButtonDefaults.filledTonalButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                onClick = {
                onDismissRequest()
            }) {
               Text(text = "Dismiss")
            }
        },
        title = {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
        },
        text = {
            Text(
                style = MaterialTheme.typography.labelMedium,
                text = message
            )
        },
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
            usePlatformDefaultWidth = false
        )

    )
}