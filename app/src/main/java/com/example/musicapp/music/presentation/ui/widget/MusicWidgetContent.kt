package com.example.musicapp.music.presentation.ui.widget

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.annotation.DrawableRes
import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.AndroidResourceImageProvider
import androidx.glance.BitmapImageProvider
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.action.Action
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.components.SquareIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.RowScope
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.MainActivity
import com.example.musicapp.R
import com.example.musicapp.constants.IconSize
import com.example.musicapp.service.MusicService

private val textColor = Color(0xFFf07fb4)
private val backgroundColor = Color(0xFFffdbe8)
private val iconTintColor = Color(0xFFf2dbea)
private val iconBackgroundColor = Color(0xFFf07fb4)

@OptIn(UnstableApi::class)
@SuppressLint("RestrictedApi")
@Composable
fun MusicWidgetContent(
    title: String = "Title",
    isPlaying: Boolean,
    repeatMode: Int,
    shuffleMode: Boolean,
    bitmap: Bitmap? = null
) {
    Row(
        modifier = GlanceModifier.fillMaxSize().background(backgroundColor).padding(8.dp)
            .clickable(onClick = actionStartActivity<MainActivity>()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.CenterVertically
    ) {
        bitmap?.let {
            Image(
                provider = BitmapImageProvider(it),
                contentDescription = null,
                modifier = GlanceModifier.size(70.dp).cornerRadius(8.dp),
                contentScale = ContentScale.Crop
            )
        } ?: Image(
            provider = AndroidResourceImageProvider(R.drawable.image),
            contentDescription = null,
            modifier = GlanceModifier.size(70.dp).cornerRadius(8.dp),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = GlanceModifier.fillMaxSize().padding(start = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title, maxLines = 1,
                style = TextStyle(
                    color = ColorProvider(textColor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            Row(
                modifier = GlanceModifier.defaultWeight().fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlanceIcon(
                    if (shuffleMode) R.drawable.ic_shuffle
                    else R.drawable.ic_shuffle_off,
                    onClick = PlayActionCallBack.create(MusicService.ACTION_SHUFFLE),
                )
                GlanceIcon(
                    R.drawable.ic_skip_back,
                    onClick = PlayActionCallBack.create(MusicService.ACTION_PREVIOUS),
                )
                GlanceIcon(
                    if (isPlaying) R.drawable.ic_pause
                    else R.drawable.ic_play,
                    onClick = PlayActionCallBack.create(MusicService.ACTION_PLAY_OR_PAUSE),
                )
                GlanceIcon(
                    R.drawable.ic_skip_forward,
                    onClick = PlayActionCallBack.create(MusicService.ACTION_NEXT)
                )
                GlanceIcon(
                    when (repeatMode) {
                        Player.REPEAT_MODE_ALL -> R.drawable.ic_repeat
                        Player.REPEAT_MODE_ONE -> R.drawable.ic_repeat_one
                        else -> R.drawable.ic_repeat_off
                    }, onClick = PlayActionCallBack.create(MusicService.ACTION_REPEAT)
                )
            }
        }
    }
}


@SuppressLint("RestrictedApi")
@Composable
fun RowScope.GlanceIcon(@DrawableRes resId: Int, onClick: Action) {
    Box(
        modifier = GlanceModifier.fillMaxHeight().defaultWeight(),
        contentAlignment = Alignment.BottomCenter
    ) {
        SquareIconButton(
            AndroidResourceImageProvider(resId),
            contentDescription = null,
            onClick = onClick,
            modifier = GlanceModifier.size(IconSize),
            contentColor = ColorProvider(iconTintColor),
            backgroundColor = ColorProvider(iconBackgroundColor)
        )
    }
}