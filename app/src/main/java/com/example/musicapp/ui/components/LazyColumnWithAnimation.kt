package com.example.musicapp.ui.components

import ExperimentalLazyColumn
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.musicapp.domain.model.Identifiable

@ExperimentalLazyColumn
@Composable
fun LazyColumnWithAnimation(
    modifier: Modifier = Modifier,
    items: List<Identifiable>,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    animationDuration: Int = 200,
    initialOffsetX: Float = 100f,
    content: @Composable LazyListScope.(itemModifier: Modifier, index: Int, item: Identifiable) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(items = items, key = { _, item -> item.id }) { index, item ->
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


private data class Item(
    override val id: String,
    val title: String
) : Identifiable

@Preview
@Composable
private fun LazyColumnWithAnimationSample() {
    val items = remember { List(20) { Item(it.toString(), "Item $it") } }

    LazyColumnWithAnimation(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFf5b4d5)),
        items = items,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) { itemModifier, _, item ->
        Box(
            modifier = itemModifier
                .padding(4.dp)
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFFf9f7fb))
        ) {
            Text(
                text = (item as Item).title,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}



@Composable
inline fun <T> LazyColumnWithAnimation2(
    modifier: Modifier = Modifier,
    items: List<T>,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    animationDuration: Int = 200,
    initialOffsetX: Float = 100f,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline content: @Composable LazyListScope.(itemModifier: Modifier, index: Int, item: T) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(items = items, key = { index, item -> key?.invoke(index, item) ?: item.hashCode() }) { index, item ->
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
