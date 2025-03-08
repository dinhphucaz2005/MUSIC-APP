package com.example.musicapp.song

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapp.constants.DefaultCornerSize
import com.example.musicapp.constants.SongItemHeight
import com.example.musicapp.core.presentation.components.MyListItem
import com.example.musicapp.core.presentation.components.Thumbnail
import com.example.musicapp.di.FakeModule
import com.example.musicapp.other.domain.model.Song
import com.example.musicapp.ui.theme.Black
import com.example.musicapp.ui.theme.LightGray
import com.example.musicapp.ui.theme.MyMusicAppTheme
import com.example.musicapp.ui.theme.White

@Preview
@Composable
private fun MiniSongItemContentPreview() {
    MyMusicAppTheme {
        MiniSongItemContent(FakeModule.localSong)
    }
}

@Preview
@Composable
private fun SongItemContentPreview() {
    MyMusicAppTheme {
        Column {
            SongItemContent(
                song = FakeModule.localSong
            ) { }
            SongItemContent2(
                song = FakeModule.localSong,
                onSongClick = {}
            ) { }
        }
    }
}


@Composable
fun MiniSongItemContent(song: Song) {
    MyListItem(headlineContent = {
        Text(
            text = song.getSongTitle(),
            style = MaterialTheme.typography.titleMedium,
            color = White,
            maxLines = 1,
            modifier = Modifier.basicMarquee(
                iterations = Int.MAX_VALUE, spacing = MarqueeSpacing.fractionOfContainer(
                    1f / 10f
                )
            )
        )
    }, leadingContent = {
        Thumbnail(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(DefaultCornerSize))
                .fillMaxHeight()
                .aspectRatio(1f), thumbnailSource = song.getThumbnail()
        )
    }, supportingContent = {
        Text(
            text = song.getSongArtist() + " \u2022 " + song.getDuration(),
            style = MaterialTheme.typography.titleSmall,
            color = LightGray
        )
    }, modifier = Modifier
        .padding(horizontal = 12.dp)
        .fillMaxWidth()
        .height(60.dp)
    )

}

@Composable
fun SongItemContent2(
    modifier: Modifier = Modifier, song: Song,
    onSongClick: () -> Unit, onMoreChoice: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSongClick)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .clip(RoundedCornerShape(DefaultCornerSize))
            .size(56.dp)
            .aspectRatio(1f)

        Thumbnail(
            modifier = imageModifier,
            thumbnailSource = song.getThumbnail(),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.getSongTitle(),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${song.getSongArtist()} \u00B7 ${song.getDuration()}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        IconButton(onClick = onMoreChoice) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = Black,
            )
        }
    }
}


@Composable
fun SongItemContent(
    modifier: Modifier = Modifier, song: Song, onMoreChoice: () -> Unit
) {
    MyListItem(headlineContent = {
        Text(
            text = song.getSongTitle(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = White,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
        )
    }, leadingContent = {
        val imageModifier = Modifier
            .clip(RoundedCornerShape(DefaultCornerSize))
            .fillMaxHeight()
            .aspectRatio(1f)
        Thumbnail(
            modifier = imageModifier, thumbnailSource = song.getThumbnail()
        )
    }, supportingContent = {
        Text(
            text = "${song.getSongArtist()} \u00B7 ${song.getDuration()}", color = White
        )
    }, trailingContent = {
        IconButton(onClick = onMoreChoice) {
            Icon(
                imageVector = Icons.Default.MoreVert, contentDescription = null,
                tint = White,
            )
        }
    }, modifier = modifier
        .height(SongItemHeight)
        .fillMaxWidth()
    )
}

@Composable
fun AnimatedBorder(
    modifier: Modifier = Modifier,
    colors: List<Color> = emptyList(),
    content: @Composable (Modifier) -> Unit
) {

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val rotation = infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            tween(4000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val brush = if (colors.isEmpty()) {
        Brush.sweepGradient(
            listOf(
                Color(0xFF1ac6f1),
                Color(0xFFf636bf),
                Color(0xFFfe3875),
                Color(0xFF8171ff)
            )
        )
    } else {
        Brush.sweepGradient(colors)
    }

    Surface(
        modifier = modifier, shape = RoundedCornerShape(32.dp), color = Black
    ) {
        Surface(
            modifier = Modifier
                .clipToBounds()
                .fillMaxWidth()
                .drawWithContent {
                    rotate(rotation.value) {
                        drawCircle(
                            brush = brush, radius = size.width, blendMode = BlendMode.SrcIn
                        )
                    }
                    drawContent()
                }
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            color = Black
        ) {
            content(
                Modifier
                    .clipToBounds()
                    .fillMaxSize()
            )
        }

    }


}
