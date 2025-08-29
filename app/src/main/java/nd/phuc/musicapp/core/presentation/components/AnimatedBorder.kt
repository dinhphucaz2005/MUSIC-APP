package nd.phuc.musicapp.core.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp


@Composable
fun AnimatedBorder(
    modifier: Modifier = Modifier,
    colors: List<Color> = emptyList(),
    content: @Composable (Modifier) -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val brush = if (colors.isEmpty()) {
        Brush.sweepGradient(
            listOf(
                Color(0xFF1ac6f1),
                Color(0xFFf636bf),
                Color(0xFFfe3875),
                Color(0xFF8171ff)
            )
        )
    } else {
        Brush.sweepGradient(colors)
    }

    Surface(
        modifier = modifier, shape = RoundedCornerShape(32.dp), color = MaterialTheme.colorScheme.primary
    ) {
        Surface(
            modifier = Modifier
                .clipToBounds()
                .fillMaxWidth()
                .drawWithContent {
                    rotate(rotation.value) {
                        drawCircle(
                            brush = brush, radius = size.width, blendMode = BlendMode.SrcIn
                        )
                    }
                    drawContent()
                }
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.primary
        ) {
            content(
                Modifier
                    .clipToBounds()
                    .fillMaxSize()
            )
        }

    }


}
