package com.example.musicapp.music.domain.repository

import com.example.musicapp.music.domain.model.UserSetting

interface UserRepository {

    suspend fun getUserSetting(): UserSetting

    suspend fun saveUserSetting(userSetting: UserSetting)
}