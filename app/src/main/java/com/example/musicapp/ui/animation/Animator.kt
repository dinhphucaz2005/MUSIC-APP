package com.example.musicapp.ui.animation

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

class Animator {
    companion object {

        private const val ANIMATION_DURATION = 600
        private val ANIMATION_EASING = EaseInOut

        val enterAnimation = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = ANIMATION_EASING
            )
        ) + scaleIn(
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = ANIMATION_EASING
            )
        )
        val exitAnimation = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = ANIMATION_EASING
            )
        ) + scaleOut(
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = ANIMATION_EASING
            )
        )
    }
}