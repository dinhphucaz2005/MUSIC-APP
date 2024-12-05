package com.example.musicapp.other.presentation.ui.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.GlanceId
import androidx.glance.action.Action
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import com.example.musicapp.service.MusicService

object PlayActionCallBack : ActionCallback {

    const val TAG = "PlayActionCallBack"
    private const val KEY = "action"
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        Log.d(TAG, "onAction: ${parameters[ActionParameters.Key(KEY)]}")
        val action: String? = parameters[ActionParameters.Key(KEY)]
        if (action == null) {
            Log.d(TAG, "onAction: action is null")
            return
        }
        val playIntent = Intent(context, MusicService::class.java)
        playIntent.setAction(action)
        context.startService(playIntent)
    }

    fun create(action: String): Action = actionRunCallback<PlayActionCallBack>(
        actionParametersOf(ActionParameters.Key<String>(KEY) to action)
    )

}
