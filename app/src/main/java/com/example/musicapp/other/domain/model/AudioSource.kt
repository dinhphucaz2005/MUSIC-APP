package com.example.musicapp.other.domain.model

import android.net.Uri
import com.example.innertube.models.SongItem

sealed class AudioSource {
    data class FromUrl(val url: String) : AudioSource()
    data class FromLocalFile(val uri: Uri) : AudioSource()
}