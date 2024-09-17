package com.example.musicapp.data.api.dto

import com.google.gson.annotations.SerializedName

data class SongDTO(
    @SerializedName("author") val author: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String
)