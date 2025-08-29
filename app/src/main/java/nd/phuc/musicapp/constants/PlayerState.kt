package nd.phuc.musicapp.constants

import androidx.annotation.DrawableRes
import nd.phuc.musicapp.R

enum class PlayerState(
    @DrawableRes val resource: Int
) {
    PAUSE(R.drawable.ic_pause),
    PLAY(R.drawable.ic_play);

    companion object {
        fun fromBoolean(isPlaying: Boolean?): PlayerState = if (isPlaying == true) PAUSE else PLAY
    }
}