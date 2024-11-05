package com.example.musicapp.domain.model


/*
    songInfos save:
    id: song entity id in database
    localSongId: song id in local files
 */
data class PlayList(
    val id: Long,
    val name: String,
    val songs: List<Song> = emptyList(),
    var index: Int? = null
) {

    constructor() : this(
        id = LOCAL_ID, name = LOCAL_NAME
    )

    fun getSong(): List<Song> {
        return emptyList()
    }

    companion object {
        fun getLocalPlayList(songs: List<Song> = emptyList()) =
            PlayList(LOCAL_ID, LOCAL_NAME, songs)

        const val LOCAL_ID = -1L
        private const val INVALID_ID = -2L
        private const val LOCAL_NAME = "Local PlayList"
        private const val INVALID_NAME = "Invalid PlayList"
        val INVALID_PLAYLIST = PlayList(INVALID_ID, INVALID_NAME)
    }
}