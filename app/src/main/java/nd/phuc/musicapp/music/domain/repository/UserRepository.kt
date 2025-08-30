package nd.phuc.musicapp.music.domain.repository

import nd.phuc.core.model.UserSetting

interface UserRepository {

    suspend fun getUserSetting(): UserSetting

    suspend fun saveUserSetting(userSetting: UserSetting)
}