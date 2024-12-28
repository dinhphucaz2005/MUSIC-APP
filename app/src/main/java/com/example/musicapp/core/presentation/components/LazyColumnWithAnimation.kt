package com.example.musicapp.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

@Composable
inline fun <T> LazyColumnWithAnimation2(
    modifier: Modifier = Modifier,
    items: List<T>,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    animationDuration: Int = 200,
    initialOffsetX: Float = 100f,
    state: LazyListState = rememberLazyListState(),
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline content: @Composable LazyListScope.(itemModifier: Modifier, index: Int, item: T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(items = items, key = key) { index, item ->
            val offsetX = remember { Animatable(initialOffsetX) }

            LaunchedEffect(item) {
                if (offsetX.value != 0f) {
                    offsetX.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = animationDuration)
                    )
                }
            }

            this@LazyColumn.content(
                Modifier.offset { IntOffset(offsetX.value.toInt(), 0) }, index, item
            )
        }
    }
}
