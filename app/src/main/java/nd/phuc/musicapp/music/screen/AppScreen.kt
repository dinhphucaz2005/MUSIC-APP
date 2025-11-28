package nd.phuc.musicapp.music.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import nd.phuc.core.domain.model.NavigationBarHeight
import nd.phuc.core.domain.model.UnknownSong
import nd.phuc.musicapp.LocalMediaControllerManager

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

    val mediaControllerManager = LocalMediaControllerManager.current
    val currentSong by mediaControllerManager.currentSong.collectAsStateWithLifecycle()

    // Only show player if a song has been loaded
    val hasSong = currentSong !is UnknownSong

    val state = remember {
        AnchoredDraggableState(
            initialValue = MiniPlayerState.Dismissed,
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

    // Auto-expand to collapsed when song starts playing
    if (hasSong && state.currentValue == MiniPlayerState.Dismissed) {
        coroutineScope.launch {
            state.animateTo(MiniPlayerState.Collapsed)
        }
    }

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

        // Bottom Navigation - hide when player is expanded
        bottomNavigationBar(
            Modifier
                .align(Alignment.BottomCenter)
                .height(NavigationBarHeight)
                .fillMaxWidth()
                .alpha(1f - progress) // Fade out as player expands
                .graphicsLayer {
                    translationY = progress * NavigationBarHeight.toPx()
                }
        )

        // Player (draggable) - only show if there's a song
        if (hasSong) {
            Box(
                Modifier
                    .offset { IntOffset(0, state.requireOffset().toInt()) }
                    .fillMaxWidth()
                    .height(with(density) {
                        (miniPlayerHeightPx + (expandedHeightPx - miniPlayerHeightPx) * progress).toDp()
                    })
                    .anchoredDraggable(state, Orientation.Vertical)
            ) {
                // Collapsed state: Show MiniPlayer
                if (progress < 0.5f) {
                    MiniPlayer(
                        onExpand = {
                            coroutineScope.launch {
                                state.animateTo(MiniPlayerState.Expanded)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .alpha(1f - progress * 2) // Fade out as we expand
                    )
                }

                // Expanded state: Show full SongScreenContent
                if (progress > 0f) {
                    SongScreen(
                        onBackClick = {
                            coroutineScope.launch {
                                state.animateTo(MiniPlayerState.Collapsed)
                            }
                        },
                        mediaControllerManager = mediaControllerManager,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(progress) // Fade in as we expand
                    )
                }
            }
        }
    }
}
