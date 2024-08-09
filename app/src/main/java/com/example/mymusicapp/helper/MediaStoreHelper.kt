package com.example.mymusicapp.helper

import android.media.MediaScannerConnection
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.di.AppModule

object MediaStoreHelper {

    @OptIn(UnstableApi::class)
    fun scanFile(filePath: String) {
        MediaScannerConnection.scanFile(
            AppModule.provideAppContext(),
            arrayOf(filePath),
            null,
        ) { _, _ ->
            AppModule.provideSongFileRepository().reloadFiles()
        }
    }

}