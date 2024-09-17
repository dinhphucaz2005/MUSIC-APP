package com.example.musicapp.domain.repository

import com.example.musicapp.domain.model.UserSetting

interface UserRepository {

    suspend fun getUserSetting(): UserSetting

    suspend fun saveUserSetting(userSetting: UserSetting)
}