package com.example.musicapp.domain.model


/*
    songInfos save:
    id: song entity id in database
    localSongId: song id in local files
 */
data class PlayList(
    override val id: Long,
    val name: String,
    val songs: List<Song> = emptyList(),
    var index: Int? = null,
) : Identifiable {

    constructor() : this(
        id = LOCAL_ID, name = LOCAL_NAME
    )

    fun getSong(): List<Song> {
        return emptyList()
    }

    override fun equals(other: Any?): Boolean {
        val flag =
            id == (other as PlayList).id && name == other.name && songs == other.songs && songs.size == other.songs.size
        if (!flag) return false
        for (i in songs.indices) {
            if (songs[i] != other.songs[i]) return false
        }
        return true
    }

    companion object {
        fun getLocalPlayList(songs: List<Song> = emptyList()) =
            PlayList(LOCAL_ID, LOCAL_NAME, songs)

        fun getInvalidPlayList() = PlayList(INVALID_ID, INVALID_NAME)

        const val LOCAL_ID = -1L
        private const val INVALID_ID = -2L
        private const val LOCAL_NAME = "Local PlayList"
        private const val INVALID_NAME = "Invalid PlayList"
    }
}