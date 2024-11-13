package com.example.innertube.models.body

import com.example.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class PlayerBody(
    val context: Context,
    val videoId: String,
    val playlistId: String?,
    val contentCheckOk: Boolean = true,
)
