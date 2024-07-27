package com.example.mymusicapp.extension

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun ImageBitmap.toScaledImageBitmap(factor: Float): ImageBitmap {
    val width = (this.width * factor).toInt()
    val height = (this.height * factor).toInt()
    val originalBitmap = this.asAndroidBitmap()
    val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)
    val canvas = Canvas(scaledBitmap)
    val paint = android.graphics.Paint()

    canvas.drawBitmap(originalBitmap, null, android.graphics.Rect(0, 0, width, height), paint)
    return scaledBitmap.asImageBitmap()
}

