package com.example.musicapp.other.domain.model


data class PlayList(
    val id: String,
    val name: String,
    val thumbnailSource: ThumbnailSource = ThumbnailSource.FromBitmap(null)
) {


    override fun equals(other: Any?): Boolean {
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}