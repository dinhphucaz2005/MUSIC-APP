package nd.phuc.musicapp.music.domain.repository

import nd.phuc.musicapp.music.domain.model.UserSetting

interface UserRepository {

    suspend fun getUserSetting(): UserSetting

    suspend fun saveUserSetting(userSetting: UserSetting)
}