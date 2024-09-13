package com.example.musicapp.extension

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

fun Bitmap.toScaledBitmap(scaleFactor: Float): Bitmap {
    val width = (this.width * scaleFactor).toInt()
    val height = (this.height * scaleFactor).toInt()
    val resizedBitmap = Bitmap.createScaledBitmap(this, width, height, true)
    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    val byteArray = outputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}
