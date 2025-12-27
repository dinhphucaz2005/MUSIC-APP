package nd.phuc.musicapp.helper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

fun Bitmap.toScaledBitmap(scaleFactor: Float): Bitmap {
    val width = (this.width * scaleFactor).toInt()
    val height = (this.height * scaleFactor).toInt()
    val resizedBitmap = this.scale(width, height)
    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    val byteArray = outputStream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun ByteArray.toBitmap(): Bitmap? {
    if (this.isEmpty()) return null
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

const val DEFAULT_COMPRESS_FORMAT = "webp"
val defaultCompressFormat = Bitmap.CompressFormat.WEBP

fun ImageBitmap?.toByteArray(): ByteArray? {
    if (this == null) return null

    return try {
        val bitmap = this.asAndroidBitmap()
        ByteArrayOutputStream().use { byteArrayOutputStream ->
            bitmap.compress(defaultCompressFormat, 50, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
