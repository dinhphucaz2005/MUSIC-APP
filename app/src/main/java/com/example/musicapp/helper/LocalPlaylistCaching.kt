package com.example.musicapp.helper

import java.io.File
import java.util.concurrent.ConcurrentHashMap

typealias Path = String
typealias IndexOfSong = Int
typealias DateModified = Long
typealias FileModificationData = Pair<DateModified, IndexOfSong>

object LocalPlaylistCaching {

    private val fileModificationDates = ConcurrentHashMap<Path, FileModificationData>()

    fun check(path: String): Int? {
        if (fileModificationDates.isEmpty()) return null
        val fileModificationData = fileModificationDates[path] ?: return null
        return fileModificationData.let { (storedLastModified, index) ->
            val lastModified = File(path).lastModified()
            if (storedLastModified != lastModified) {
                fileModificationDates[path] = FileModificationData(lastModified, index)
                null
            } else index
        }
    }

    fun add(path: String?, index: Int) {
        if (path == null) return
        fileModificationDates[path] = FileModificationData(File(path).lastModified(), index)
    }
}