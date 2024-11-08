package com.example.musicapp.domain.model


data class PlayList(
    override val id: Long,
    val name: String,
    val songs: List<Song> = emptyList(),
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

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + songs.hashCode()
        return result
    }

    companion object {
        fun getLocalPlayList(songs: List<Song> = emptyList()) =
            PlayList(LOCAL_ID, LOCAL_NAME, songs)

        fun getServerPlayList() = PlayList(SERVER_ID, SERVER_NAME)

        fun getInvalidPlayList() = PlayList(INVALID_ID, INVALID_NAME)

        const val INVALID_ID = -3L
        const val LOCAL_ID = -1L
        const val SERVER_ID = -2L
        private const val LOCAL_NAME = "Local PlayList"
        private const val SERVER_NAME = "Invalid PlayList"
        private const val INVALID_NAME = "Invalid PlayList"
    }
}