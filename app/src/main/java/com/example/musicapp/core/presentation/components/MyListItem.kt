package com.example.musicapp.core.presentation.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.musicapp.other.presentation.ui.theme.White

@Composable
fun MyListItem(
    headlineContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    overlineContent: @Composable (() -> Unit)? = null,
    supportingContent: @Composable (() -> Unit)? = null,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(
        containerColor = Color.Transparent,
        trailingIconColor = White,
        headlineColor = White,
        leadingIconColor = White,
        overlineColor = White,
        supportingColor = White,
    ),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation,
) {
    ListItem(
        headlineContent,
        modifier,
        overlineContent,
        supportingContent,
        leadingContent,
        trailingContent,
        colors,
        tonalElevation,
        shadowElevation
    )
}