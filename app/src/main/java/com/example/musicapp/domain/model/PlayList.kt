package com.example.musicapp.domain.model


/*
    songInfos save:
    id: song entity id in database
    localSongId: song id in local files
 */
data class PlayList(
    val id: Long,
    val name: String,
    val songInfos: List<SongInfo> = emptyList(),
    var index: Int? = null
) {

    constructor() : this(
        id = LOCAL_ID, name = LOCAL_NAME
    )

    fun getSong(): List<Song> {
        return emptyList()
    }

    fun updateSongs(newSongInfos: List<SongInfo>): PlayList {
        return copy(songInfos = newSongInfos)
    }

    companion object {
        const val LOCAL_ID = -1L
        private const val INVALID_ID = -2L
        private const val INVALID_NAME = "Local Music"
        private const val LOCAL_NAME = "Local Music"
        val LOCAL_PLAYLIST = PlayList(LOCAL_ID, LOCAL_NAME)
        val INVALID_PLAYLIST = PlayList(INVALID_ID, INVALID_NAME)
    }
}