package com.example.musicapp.other.domain.repository

import com.example.musicapp.other.domain.model.UserSetting

interface UserRepository {

    suspend fun getUserSetting(): UserSetting

    suspend fun saveUserSetting(userSetting: UserSetting)
}