package com.example.musicapp.other.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.innertube.CustomYoutube
import com.example.musicapp.constants.PREFERENCE_KEY_COOKIE
import com.example.musicapp.constants.PREFERENCE_KEY_VISITOR_DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val editor: SharedPreferences.Editor
) : ViewModel() {

    companion object {

    }

    fun saveCookie(cookie: String) {
        CustomYoutube.cookie = cookie
        editor.putString(PREFERENCE_KEY_COOKIE, cookie).apply()
    }

    fun saveVisitorData(visitorData: String) {
        CustomYoutube.visitorData = visitorData
        editor.putString(PREFERENCE_KEY_VISITOR_DATA, visitorData).apply()
    }


}