package com.example.musicapp.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicapp.core.presentation.theme.MusicTheme
import com.example.musicapp.core.presentation.theme.backgroundTemp

@Preview(backgroundColor = backgroundTemp, showBackground = true)
@Composable
fun Preview() {
    MusicTheme {
        ColorDetail()
    }
}

data class Test(
    val name: String,
    val color: Long
)

val list = listOf(
    Test("Background", 0xFF1a2730),
    Test("Primary", 0xFFafcee2),
    Test("Secondary", 0xFF46586c),
    Test("OnSecondary", 0xFFafcee2),
    Test("Error", 0xFFFF5252),
    Test("OnError", 0xFF1f1f1f),
    Test("Tertiary", 0xFFafcee2),
    Test("OnTertiary", 0xFF1d2a34)
)

@Composable
fun ColorDetail() {
    Column {
        list.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .background(Color(item.color))
                        .border(width = 2.dp, color = MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}