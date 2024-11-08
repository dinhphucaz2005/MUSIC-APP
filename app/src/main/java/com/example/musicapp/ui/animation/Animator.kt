package com.example.musicapp.ui.animation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

class Animator {
    companion object {

        private const val ANIMATION_DURATION = 700
        private const val EXIT_DURATION = 500

        private val ANIMATION_EASING = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f)

        val enterAnimation = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = ANIMATION_EASING
            )
        ) + scaleIn(
            initialScale = 0.9f,
            animationSpec = tween(
                durationMillis = ANIMATION_DURATION,
                easing = ANIMATION_EASING
            )
        )

        val exitAnimation = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(
                durationMillis = EXIT_DURATION,
                easing = ANIMATION_EASING
            )
        ) + scaleOut(
            targetScale = 0.9f,
            animationSpec = tween(
                durationMillis = EXIT_DURATION,
                easing = ANIMATION_EASING
            )
        )
    }
}