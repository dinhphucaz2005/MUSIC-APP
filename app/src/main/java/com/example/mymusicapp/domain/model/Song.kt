package com.example.mymusicapp.domain.model

import android.graphics.Bitmap

data class Song(
    val title: String?,
    val uri: String?,
    val thumbnail: Bitmap?
)