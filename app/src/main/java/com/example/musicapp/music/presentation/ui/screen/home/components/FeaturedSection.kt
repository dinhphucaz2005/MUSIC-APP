package com.example.musicapp.music.presentation.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicapp.R
import com.example.musicapp.ui.theme.darkGray
import com.example.musicapp.ui.theme.lightGray
import com.example.musicapp.ui.theme.white

@Composable
fun FeaturedSection() {
    Column {
        Text(
            text = "Featured",
            style = MaterialTheme.typography.titleLarge,
            color = white,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(darkGray)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Discover Weekly",
                    style = MaterialTheme.typography.titleMedium,
                    color = white
                )
                Text(
                    text = "Updated every Monday",
                    style = MaterialTheme.typography.bodyMedium,
                    color = lightGray
                )
            }

            Icon(
                painter = painterResource(R.drawable.playlist),
                contentDescription = "Play",
                tint = white,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .size(36.dp)
                    .background(Color(0x33FFFFFF), CircleShape)
                    .padding(8.dp)
            )
        }
    }
}