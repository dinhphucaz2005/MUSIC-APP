package com.example.musicapp.extension

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

fun MediaMetadataRetriever.getImageBitmap(): ImageBitmap? =
    if (embeddedPicture == null)
        null
    else
        BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture!!.size)
            .asImageBitmap()

fun MediaMetadataRetriever.getDuration(): Long? =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull()

fun MediaMetadataRetriever.getTitle(): String? =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)

fun MediaMetadataRetriever.getAuthor(): String =
    extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: "Unknown"
