package nd.phuc.musicapp.music.data.repository

import android.content.SharedPreferences
import nd.phuc.musicapp.music.domain.model.UserSetting
import nd.phuc.musicapp.music.domain.repository.UserRepository
import javax.inject.Inject
import androidx.core.content.edit

class UserRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : UserRepository {

    companion object {
        private const val KEY_BACKGROUND_COLOR = "background_color"
        private const val KEY_PRIMARY_COLOR = "primary_color"
    }

    override suspend fun getUserSetting(): UserSetting {
        var backgroundColor: Long? = sharedPreferences.getLong(KEY_BACKGROUND_COLOR, -1)
        var primaryColor: Long? = sharedPreferences.getLong(KEY_PRIMARY_COLOR, -1)
        if (backgroundColor == -1L) backgroundColor = null
        if (primaryColor == -1L) primaryColor = null
        return UserSetting(backgroundColor = backgroundColor, primaryColor = primaryColor)
    }

    override suspend fun saveUserSetting(userSetting: UserSetting) {
        sharedPreferences.edit {
            putLong(KEY_BACKGROUND_COLOR, userSetting.backgroundColor ?: -1)
                .putLong(KEY_PRIMARY_COLOR, userSetting.primaryColor ?: -1)
        }
    }
}