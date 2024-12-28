package com.example.musicapp.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.musicapp.constants.IconSize
import com.example.musicapp.ui.theme.White

@Composable
fun CommonIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    size: Dp = IconSize,
    tint: Color = White,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    Icon(
        painter = painterResource(icon),
        modifier = modifier
            .clickable(enabled = enabled, onClick = { onClick?.invoke() })
            .size(size),
        contentDescription = null,
        tint = tint
    )
}
