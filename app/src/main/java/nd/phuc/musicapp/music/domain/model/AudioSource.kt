package nd.phuc.core.model

import android.net.Uri
import nd.phuc.core.model.AudioSource

sealed class AudioSource {
    data class FromUrl(val url: String) : AudioSource()
    data class FromLocalFile(val uri: Uri) : AudioSource()
}