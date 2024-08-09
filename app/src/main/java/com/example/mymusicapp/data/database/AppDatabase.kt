package com.example.mymusicapp.data.database

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity(tableName = "play_lists")
data class PlayListsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "image_data") val imageData: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayListsEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (imageData != null) {
            if (other.imageData == null) return false
            if (!imageData.contentEquals(other.imageData)) return false
        } else if (other.imageData != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + (imageData?.contentHashCode() ?: 0)
        return result
    }
}

@Entity(tableName = "songs")
data class SongsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "content_uri") val contentURI: String,
    @ColumnInfo(name = "play_list_id") val playListId: Int,
)

@Database(entities = [SongsEntity::class, PlayListsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDAO(): AppDAO
}