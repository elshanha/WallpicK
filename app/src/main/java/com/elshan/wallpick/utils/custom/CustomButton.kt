package com.elshan.wallpick.utils.custom

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun CustomButton(
    modifier: Modifier,
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .heightIn(min = 50.dp)
            .widthIn(max = 350.dp)
            .padding(end = 5.dp)
        ,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary
        ),
        border = null,
        elevation = null,
        contentPadding = PaddingValues(horizontal = 15.dp),
        onClick = onClick
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Confirm",
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}



//        Box(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .horizontalWindowInsetsPadding()
//                .bottomWindowInsetsPadding()
//                .padding(horizontal = 20.dp)
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceAround
//                ) {
//
//                    CustomButton(
//                        modifier = Modifier.weight(1f),
//                        text = "Categories",
//                        icon = Icons.Outlined.Category
//                    ) {
//                        navController.navigate(
//                            Screen.Category
//                        )
//                    }
//
//                    CustomButton(
//                        modifier = Modifier.weight(1f),
//                        text = "Favorites",
//                        icon = Icons.Outlined.Favorite
//                    ) {
//                        navController.navigate(
//                            Screen.Favorites
//                        )
//                    }
//                    CustomButton(
//                        modifier = Modifier.weight(1f),
//                        text = "Profile",
//                        icon = Icons.Outlined.AccountCircle
//                    ) {
//                        navController.navigate(
//                            Screen.Profile(
//                                title = "Profile", description = "Description"
//                            )
//                        )
//                    }
//                }
//            }
//        }