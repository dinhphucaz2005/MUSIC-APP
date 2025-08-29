package nd.phuc.musicapp.core.presentation.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "phone", device = "spec:width=360dp,height=640dp,dpi=480")
@Preview(name = "landscape", device = "spec:width=640dp,height=360dp,dpi=480")
@Preview(name = "foldable", device = "spec:width=673dp,height=841dp,dpi=480")
@Preview(name = "tablet", device = "spec:width=1280dp,height=800dp,dpi=480")
annotation class ExtendDevicePreviews


@LightPreview
@DarkPreview
annotation class DefaultPreview

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark", backgroundColor = 0xFF171C26)
annotation class DarkPreview

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light", backgroundColor = 0xFF9BB3FF)
annotation class LightPreview
