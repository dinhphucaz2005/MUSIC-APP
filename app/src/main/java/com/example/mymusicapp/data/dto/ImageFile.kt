package com.example.mymusicapp.data.dto

import android.graphics.Bitmap
import android.net.Uri

data class ImageFile(
    val uri: Uri,
    var bitmap: Bitmap? = null
)
