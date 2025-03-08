package com.example.musicapp.extension

import com.example.player.model.Artist


fun List<Artist>.toArtistString(): String {
    val stringBuilder = StringBuilder()
    for (artist in this) {
        stringBuilder.append(artist.name)
        if (this.indexOf(artist) != this.size - 1) {
            stringBuilder.append(", ")
        }
    }
    return stringBuilder.toString()
}