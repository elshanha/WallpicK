package com.elshan.wallpick.presentation.screen.details.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TextBox(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    text: String,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = imageVector, contentDescription = null)
        Spacer(modifier = modifier.width(8.dp))
        Text(text = text, style = textStyle)
    }
}