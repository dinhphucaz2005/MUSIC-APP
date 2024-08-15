@file:OptIn(ExperimentalFoundationApi::class)

package com.example.mymusicapp.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.background
import com.example.mymusicapp.ui.theme.tempColor
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.example.mymusicapp.R
import com.example.mymusicapp.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MySearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {}
) {

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchHistory = listOf(
        "Noi dau tu mot nguoi den sau",
        "Su that sau mot loi hua",
        "Giac mo khong phai anh",
        "Yeu thuong gio xa anh qua",
        "Hay tin anh lan nua",
        "....."
    )

    DockedSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { onSearch(it) },
        active = active,
        onActiveChange = { active = it },
        modifier = modifier,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(tempColor)
            )
        },
        trailingIcon = {
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mic),
                        contentDescription = null,
                        tint = Color(tempColor)
                    )
                }
                if (active) {
                    IconButton(onClick = {
                        if (query.isNotEmpty()) query = "" else active = false
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close, contentDescription = null,
                            tint = Color(tempColor)
                        )
                    }
                }
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = Color(tempColor),
            dividerColor = Color(tempColor),
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = Color(tempColor),
                unfocusedTextColor = Color(tempColor),
                cursorColor = Color(tempColor)
            )
        )
    ) {
        Column(
            modifier = Modifier.background(
                color = Color(tempColor).copy(0.5f)
            )
        ) {
            searchHistory.forEach { item ->
                ListItem(
                    modifier = Modifier
                        .background(color = Color(tempColor))
                        .clickable { query = item },
                    headlineContent = { Text(text = item) }, leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = null,
                            tint = Color(tempColor)
                        )
                    },
                    colors = ListItemDefaults.colors(
                        containerColor = Color(tempColor),
                        headlineColor = Color(tempColor),
                        leadingIconColor = Color(tempColor)
                    )
                )
            }
        }
    }
}


@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun SongPreview(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = hiltViewModel(),
    onClick: () -> Unit = {},
) {
    val playingState by viewModel.isPlaying().collectAsState()

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .background(Color(0xFF1c6387))
            .height(72.dp)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(color = Color(0xFF77b2e0))
                .fillMaxHeight()
                .aspectRatio(1f)
        ) {
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "song?.title.toString()",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .basicMarquee(
                        delayMillis = 0,
                        iterations = Int.MAX_VALUE,
                        spacing = MarqueeSpacing.fractionOfContainer(1f / 10f)
                    )
                    .clickable { onClick() },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(tempColor)
            )
            Text(
                text = "song?.artist.toString()",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable { onClick() },
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = Color(tempColor)
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = Color(tempColor),
            )
        }
        Icon(
            painter = painterResource(
                playingState.resource
            ),
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable {
                    viewModel.playOrPause()
                },
            tint = Color(tempColor)
        )
        IconButton(onClick = {
            viewModel.playNext()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(tempColor)
            )
        }
    }
}