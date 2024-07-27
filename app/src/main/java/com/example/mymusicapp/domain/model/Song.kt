package com.example.mymusicapp.domain.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.ui.graphics.ImageBitmap

data class Song(
    val fileName: String = "",
    val uri: Uri? = null,
    val path: String? = null,
    val title: String? = null,
    val artist: String? = null,
    val imageBitmap: ImageBitmap? = null,
    val duration: Long? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        null,
        parcel.readValue(Long::class.java.classLoader) as? Long
    )

    operator fun times(i: Int): List<Song> {
        return List(i) { this }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}