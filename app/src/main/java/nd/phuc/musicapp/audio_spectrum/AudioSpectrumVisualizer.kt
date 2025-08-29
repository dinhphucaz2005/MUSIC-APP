package nd.phuc.musicapp.audio_spectrum

import android.media.audiofx.Visualizer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.pow


const val NUM_BARS = 60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioWaveformVisualizer(audioSessionId: Int, modifier: Modifier = Modifier) {
    var targetWaveform by remember { mutableStateOf(FloatArray(NUM_BARS) { 0f }) }
    var isExpanded by remember { mutableStateOf(false) }
    var windowFunction by remember { mutableStateOf(WindowFunction.HANNING) }

    // Danh sách Animatable cho từng bar để làm mượt
    val animatedBars = remember { List(NUM_BARS) { Animatable(0f) } }

    // Hiệu ứng cập nhật giá trị khi sóng âm thay đổi
    LaunchedEffect(targetWaveform) {
        targetWaveform.forEachIndexed { index, value ->
            launch {
                animatedBars[index].animateTo(value, animationSpec = tween(300))
            }
        }
    }

    DisposableEffect(audioSessionId) {
        val visualizer = Visualizer(audioSessionId).apply {
//            Visualizer.setCaptureSize = Visualizer.getCaptureSizeRange()[1]
            setDataCaptureListener(object : Visualizer.OnDataCaptureListener {
                override fun onWaveFormDataCapture(
                    visualizer: Visualizer?,
                    waveformBytes: ByteArray?,
                    samplingRate: Int
                ) {
                    waveformBytes?.let {
                        val newWaveform = decodePCM16BitAndApplyWindowFunctionAndCreateWaveform(
                            it,
                            windowFunction
                        )
                        targetWaveform = newWaveform
                    }
                }

                override fun onFftDataCapture(
                    visualizer: Visualizer?,
                    fft: ByteArray?,
                    samplingRate: Int
                ) {
                }
            }, Visualizer.getMaxCaptureRate() / 3, true, false)
//            Visualizer.setEnabled = true
        }
        onDispose {
            visualizer.release()
        }
    }

    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
        ) {
            TextField(
                value = windowFunction.name,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryEditable, true),
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
            )
            ExposedDropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
                WindowFunction.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.name) },
                        onClick = {
                            windowFunction = option
                            isExpanded = false
                        }
                    )
                }
            }
        }

        val infiniteTransition = rememberInfiniteTransition()

        val hue by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing), // 4 giây xoay hết vòng màu
                repeatMode = RepeatMode.Restart
            )
        )

        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            val barWidth = size.width / NUM_BARS
            animatedBars.forEachIndexed { index, animatable ->
                val barHeight = animatable.value * size.height

                // Mỗi thanh bar có hue khác nhau theo vị trí
                val barHue = (hue + index * (360f / NUM_BARS)) % 360f
                val barColor = Color.hsv(barHue, 1f, 1f) // Độ bão hòa (S) và độ sáng (V) = 1

                drawRect(
                    brush = Brush.verticalGradient(listOf(barColor, barColor.copy(alpha = 0.5f))),
                    topLeft = Offset(index * barWidth, (size.height - barHeight) / 2),
                    size = Size(barWidth - 5.dp.toPx(), barHeight)
                )
            }
        }

    }
}

enum class WindowFunction {
    HANNING, HAMMING, BLACKMAN, BARTLETT, GAUSSIAN, NONE
}

fun decodePCM16BitAndApplyWindowFunctionAndCreateWaveform(
    waveform: ByteArray,
    windowType: WindowFunction = WindowFunction.HANNING
): FloatArray {
    val size = waveform.size / 2

    val pcmData = when (windowType) {
        WindowFunction.HANNING -> FloatArray(size) { i -> (0.5 * (1 - cos(2 * PI * i / (size - 1)))).toFloat() }
        WindowFunction.HAMMING -> FloatArray(size) { i -> (0.54 - 0.46 * cos(2 * PI * i / (size - 1))).toFloat() }
        WindowFunction.BLACKMAN -> FloatArray(size) { i ->
            (0.42 - 0.5 * cos(2 * PI * i / (size - 1)) + 0.08 * cos(
                4 * PI * i / (size - 1)
            )).toFloat()
        }

        WindowFunction.BARTLETT -> FloatArray(size) { i -> (1 - (2 * abs(i - (size - 1) / 2).toFloat() / (size - 1))) }
        WindowFunction.GAUSSIAN -> FloatArray(size) { i ->
            exp(
                -0.5 * ((i - (size - 1) / 2.0) / (0.4 * (size - 1) / 2)).pow(
                    2
                )
            ).toFloat()
        }

        WindowFunction.NONE -> FloatArray(size) { 1f }
    }

    for (i in 0 until size) {
        val low = waveform[i * 2].toInt() and 0xFF
        val high = waveform[i * 2 + 1].toInt() shl 8
        val sample = (high or low).toShort()

        pcmData[i] *= (sample / 32768.0f)
    }

    val result = FloatArray(NUM_BARS)
    val step = size / NUM_BARS
    for (i in 0 until NUM_BARS)
        result[i] = pcmData[i * step]
    return result
}
