package nd.phuc.core.presentation.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.Alignment

class Animator {
    companion object {

        private const val ANIMATION_DURATION = 100

        //        private val ANIMATION_EASING = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1.0f)
        private val ANIMATION_EASING = LinearEasing

        val enterAnimation = expandVertically(
            animationSpec = tween(durationMillis = ANIMATION_DURATION, easing = ANIMATION_EASING),
            expandFrom = Alignment.CenterVertically,
            initialHeight = { it }
        )

        val exitAnimation = slideOutVertically(
            targetOffsetY = { -it }, animationSpec = tween(
                durationMillis = ANIMATION_DURATION, easing = ANIMATION_EASING
            )
        ) + scaleOut(
            targetScale = 0f, animationSpec = tween(
                durationMillis = ANIMATION_DURATION, easing = ANIMATION_EASING
            )
        )
    }
}