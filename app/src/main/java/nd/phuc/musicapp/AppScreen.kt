package nd.phuc.musicapp

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.NavigationBarHeight

enum class MiniPlayerState { Dismissed, Collapsed, Expanded }


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppScreen(
    bottomNavigationBar: @Composable (Modifier) -> Unit,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    val screenHeightPx = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val miniPlayerHeightPx = with(density) { 64.dp.toPx() }
    val navHeightPx = with(density) { NavigationBarHeight.toPx() }
    val expandedHeightPx = screenHeightPx
    val coroutineScope = rememberCoroutineScope()

    val state = remember {
        AnchoredDraggableState(
            initialValue = MiniPlayerState.Collapsed,
            anchors = DraggableAnchors {
                MiniPlayerState.Dismissed at screenHeightPx
                MiniPlayerState.Collapsed at screenHeightPx - miniPlayerHeightPx - navHeightPx
                MiniPlayerState.Expanded at 0f
            },
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { with(density) { 200.dp.toPx() } },
            snapAnimationSpec = tween(400),
            decayAnimationSpec = splineBasedDecay(density)
        )
    }

    // FAB offset theo state
    val fabOffsetX by animateDpAsState(
        if (state.currentValue == MiniPlayerState.Expanded) 72.dp else 0.dp,
        label = "fabOffsetX"
    )

    val progress by remember {
        derivedStateOf {
            val collapsed = screenHeightPx - miniPlayerHeightPx - navHeightPx
            val expanded = 0f
            val current = state.requireOffset().coerceIn(expanded, collapsed)
            1f - (current - expanded) / (collapsed - expanded) // 0f = collapsed, 1f = expanded
        }
    }

    Box(Modifier.fillMaxSize()) {
        // Content
        content()
        // Bottom Navigation
        bottomNavigationBar(
            Modifier
                .align(Alignment.BottomCenter)
                .height(NavigationBarHeight)
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = progress * NavigationBarHeight.toPx()
                }
        )

        // MiniPlayer (draggable)
        Box(
            Modifier
                .offset { IntOffset(0, state.requireOffset().toInt()) }
                .fillMaxWidth()
                .height(with(density) {
                    // Chiều cao cũng animate theo progress
                    (miniPlayerHeightPx + (expandedHeightPx - miniPlayerHeightPx) * progress).toDp()
                })
                .background(Color.Blue)
                .anchoredDraggable(state, Orientation.Vertical),
            contentAlignment = Alignment.Center
        ) {
            // Ví dụ: artwork to dần khi expand
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier
                        .size(64.dp + 128.dp * progress) // ảnh lớn dần
                        .background(Color.White, CircleShape)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Mini Player (${state.currentValue})",
                    color = Color.White,
                    fontSize = lerp(16.sp, 24.sp, progress) // text to dần
                )
                if (progress > 0.5f) {
                    Text(
                        "Expanded controls here",
                        color = Color.Yellow,
                        modifier = Modifier.alpha(progress)
                    )
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    when (state.currentValue) {
                        MiniPlayerState.Dismissed -> state.animateTo(MiniPlayerState.Collapsed)
                        else -> state.animateTo(MiniPlayerState.Dismissed)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = NavigationBarHeight + 16.dp)
                .offset(x = (-16).dp + fabOffsetX)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
        }
    }
}
