package nd.phuc.musicapp.music.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import nd.phuc.core.model.FirebaseSong
import nd.phuc.core.model.LocalSong
import nd.phuc.core.model.Song
import nd.phuc.core.model.YoutubeSong

@Entity(
    tableName = "song"
)
data class SongEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val otherId: String? = null,
    val title: String? = null,
    val audioSource: String,
    val thumbnail: String? = null,
    val type: Int,
    val durationMillis: Long? = null,
    @ColumnInfo(name = "playlist_id") val playlistId: Int
) {
    companion object {
        const val LOCAL_SONG = 0
        const val FIREBASE_SONG = 1
        const val YOUTUBE_SONG = 2

        fun create(song: Song, playlistId: Int): SongEntity? =
            when (song) {
                is LocalSong -> SongEntity(song, playlistId)
                is FirebaseSong -> SongEntity(song, playlistId)
                is YoutubeSong -> SongEntity(song, playlistId)
                else -> null
            }

    }


    constructor(song: LocalSong, playlistId: Int) : this(
        otherId = song.id.toString(),
        audioSource = song.uri.path.toString(),
        type = LOCAL_SONG,
        playlistId = playlistId
    )

    constructor(song: FirebaseSong, playlistId: Int) : this(
        otherId = song.id.toString(),
        title = song.title,
        audioSource = song.audioUrl,
        thumbnail = song.thumbnailSource.getThumbnailUrl(),
        type = FIREBASE_SONG,
        durationMillis = song.durationMillis,
        playlistId = playlistId
    )

    constructor(song: YoutubeSong, playlistId: Int) : this(
        otherId = song.id.toString(),
        title = song.title,
        audioSource = song.id.toString(),
        thumbnail = song.thumbnail,
        durationMillis = song.durationMillis,
        type = YOUTUBE_SONG,
        playlistId = playlistId
    )

}