package com.example.musicapp.youtube.presentation.componenets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.innertube.models.Artist
import com.example.innertube.models.SongItem
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.constants.SongItemHeight
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.core.presentation.theme.White
import com.example.musicapp.core.presentation.theme.commonShape
import com.example.musicapp.extension.toArtistString
import com.example.musicapp.extension.toDurationString
import com.example.musicapp.other.domain.model.ThumbnailSource


@Preview
@Composable
private fun SongItem2Preview() {
    Row {
        SongItem2(song = SongItem(
            id = "",
            title = "Title",
            artists = listOf(Artist("Artist", null)),
            album = null,
            duration = 0,
            thumbnail = "",
            explicit = false
        ), onClick = {})
    }
}

@Composable
fun SongItemFromYoutube(modifier: Modifier = Modifier, song: SongItem, onClick: () -> Unit) {
    MyListItem(
        headlineContent = {
            Text(
                text = song.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = White,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        },
        leadingContent = {
            val imageModifier = Modifier
                .clip(commonShape)
                .fillMaxHeight()
                .aspectRatio(1f)
            Thumbnail(
                modifier = imageModifier,
                thumbnailSource = ThumbnailSource.FromUrl(song.thumbnail)
            )
        },
        supportingContent = {
            Text(
                text = "${song.artists.toArtistString()} \u00B7 ${song.duration.toDurationString()}",
                color = White,
                modifier = Modifier.padding(top = 8.dp),
            )
        },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = null,
                    tint = White
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .height(SongItemHeight)
    )
}

@Composable
fun SongItem2(modifier: Modifier = Modifier, song: SongItem, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .aspectRatio(1f, true)
            .clickable(onClick = onClick)
    ) {
        Thumbnail(
            thumbnailSource = ThumbnailSource.FromUrl(song.thumbnail),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(DefaultCornerSize)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = song.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        )
    }
}
