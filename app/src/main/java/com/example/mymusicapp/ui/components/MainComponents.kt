package com.example.mymusicapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mymusicapp.R
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor


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
        "Yeu thuong gio xa anh qua"
    )

    DockedSearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { onSearch(it) },
        active = active,
        onActiveChange = { active = it },
        modifier = modifier,
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mic),
                        contentDescription = null
                    )
                }
                if (active) {
                    IconButton(onClick = {
                        if (query.isNotEmpty()) query = "" else active = false
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }
            }
        },
        colors = SearchBarDefaults.colors(
            containerColor = Background.copy(0.5f),
            dividerColor = Background.copy(0.5f),
        )
    ) {
        Column(
            modifier = Modifier.background(Background.copy(0.5f))
        ) {
            searchHistory.forEach { item ->
                ListItem(
                    modifier = Modifier
                        .background(IconTintColor)
                        .clickable { query = item },
                    headlineContent = { Text(text = item) }, leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}