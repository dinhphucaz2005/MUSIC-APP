package com.example.musicapp.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.musicapp.ui.theme.MusicTheme

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colorScheme.tertiary,
            inactiveTrackColor = MaterialTheme.colorScheme.tertiary.copy(0.5f),
            thumbColor = MaterialTheme.colorScheme.tertiary,
            activeTickColor = MaterialTheme.colorScheme.tertiary
        )
    )
}


@Preview
@Composable
private fun PreviewCustomSlider() {
    MusicTheme { CustomSlider(0.5f, {}, {}) }
}