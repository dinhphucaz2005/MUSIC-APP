package nd.phuc.musicapp.core.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import nd.phuc.musicapp.ui.theme.MyMusicAppTheme
import me.saket.squiggles.SquigglySlider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    SquigglySlider(
        value = value,
        onValueChange = onValueChange,
        onValueChangeFinished = onValueChangeFinished,
        modifier = modifier,
        colors = SliderDefaults.colors(
            activeTrackColor = MaterialTheme.colorScheme.onBackground,
            inactiveTrackColor = MaterialTheme.colorScheme.onBackground.copy(0.5f),
            thumbColor = MaterialTheme.colorScheme.onBackground,
            activeTickColor = MaterialTheme.colorScheme.onBackground
        )
    )
}


@Preview
@Composable
private fun PreviewCustomSlider() {
    MyMusicAppTheme { CustomSlider(0.5f, {}, {}) }
}