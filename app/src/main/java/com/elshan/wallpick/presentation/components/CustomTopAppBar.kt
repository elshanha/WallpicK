package com.elshan.wallpick.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.elshan.wallpick.R
import com.elshan.wallpick.navigation.Screen
import com.elshan.wallpick.utils.CircleProfileImage
import com.elshan.wallpick.utils.custom.FilterDialog
import com.elshan.wallpick.utils.custom.FilterType
import com.elshan.wallpick.utils.topBarColors


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController,
    title: String,
    selectedFilter: FilterType = FilterType.LATEST,
    onFilterSelected: (FilterType) -> Unit
) {
    var isDialogOpen by remember {
        mutableStateOf(false)
    }

    if (isDialogOpen) {
        FilterDialog(
            onDismissRequest = { isDialogOpen = false },
            selectedFilter = selectedFilter,
            onFilterSelected = { filterType ->
                onFilterSelected(filterType)
                isDialogOpen = false
            }
        )
    }

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        colors = topBarColors(),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            CircleProfileImage(
                navController = navController,
            )
        },
        actions = {
            IconButton(
                onClick = {
                    isDialogOpen = true
                }) {
                Icon(
                    modifier = Modifier.padding(),
                    painter = painterResource(
                        id = R.drawable.ic_filter
                    ), contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            IconButton(
                onClick = {
                    navController.navigate(Screen.Search)
                }) {
                Icon(
                    modifier = Modifier.padding(end = 16.dp),
                    painter = painterResource(
                        id = R.drawable.ic_search_square
                    ), contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}
