package com.example.mymusicapp.domain.model

import androidx.compose.ui.graphics.ImageBitmap

data class PlayList(
    val id: Int,
    val name: String = "Unnamed",
    val songs: List<Song> = emptyList(),
    val imageBitmap: ImageBitmap? = null
) {
    constructor() : this(
        0, "Unnamed", emptyList(), null
    )
}