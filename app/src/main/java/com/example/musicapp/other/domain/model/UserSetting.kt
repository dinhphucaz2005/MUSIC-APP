package com.example.musicapp.other.domain.model

data class UserSetting(
    var backgroundColor: Long? = null,
    val primaryColor: Long? = null,
    val errorColor: Long? = null,
    val imageUri: String? = null,
    val imageType: String? = null,
    val opacity: Float? = null,
    val blur: Int? = null
)