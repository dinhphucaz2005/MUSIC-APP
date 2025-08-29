package nd.phuc.musicapp.helper

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi


object MediaStoreHelper {

    @OptIn(UnstableApi::class)
    fun scanFile(
        filePath: String,
        context: Context,
        onError: ((Exception) -> Unit)? = null,
        onScanCompletedListener: (String, Uri) -> Unit
    ) {
        try {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(filePath),
                null,
            ) { path, uri ->
                onScanCompletedListener(path, uri)
            }
        } catch (e: Exception) {
            onError?.invoke(e)
        }
    }
}