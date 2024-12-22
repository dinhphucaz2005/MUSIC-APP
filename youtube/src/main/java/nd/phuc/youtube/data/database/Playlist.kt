package nd.phuc.youtube.data.database


import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int, val name: String, val email: String
)


@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User
}

@Database(entities = [User::class], version = 1)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}


