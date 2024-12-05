package com.example.musicapp.other.domain.model

import androidx.compose.ui.graphics.ImageBitmap


data class PlayList(
    override val id: String,
    val name: String,
    val thumbnailSource: ThumbnailSource = ThumbnailSource.FromBitmap(null)
) : Identifiable {


    constructor() : this(
        id = LOCAL_ID, name = LOCAL_NAME
    )

    fun getSong(): List<Song> {
        return emptyList()
    }

    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    companion object {

        fun getInvalidPlayList() = PlayList(INVALID_ID, INVALID_NAME)

        private const val INVALID_ID = "Invalid id"
        const val LOCAL_ID = "Local id"
        const val SERVER_ID = "Server id"
        private const val LOCAL_NAME = "Local PlayList"
        private const val SERVER_NAME = "Invalid PlayList"
        private const val INVALID_NAME = "Invalid PlayList"
    }
}