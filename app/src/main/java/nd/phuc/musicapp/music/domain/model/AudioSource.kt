package nd.phuc.musicapp.music.domain.model

import android.net.Uri

sealed class AudioSource {
    data class FromUrl(val url: String) : AudioSource()
    data class FromLocalFile(val uri: Uri) : AudioSource()
}