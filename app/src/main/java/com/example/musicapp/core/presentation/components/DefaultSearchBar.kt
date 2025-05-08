package com.example.musicapp.core.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.musicapp.R
import com.example.musicapp.ui.theme.OnSecondary
import com.example.musicapp.ui.theme.SearchBarBackground
import com.example.musicapp.ui.theme.white

@Composable
fun DefaultSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = white,
            backgroundColor = white
        )
    ) {
        Column(
            modifier = modifier
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                shape = RoundedCornerShape(100),
                colors = OutlinedTextFieldDefaults.colors(
                    cursorColor = OnSecondary,
                    focusedTextColor = OnSecondary,
                    unfocusedTextColor = OnSecondary,
                    focusedBorderColor = OnSecondary,
                    unfocusedBorderColor = OnSecondary,
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_hint),
                        color = OnSecondary,
                    )
                },
                leadingIcon = {
                    androidx.compose.material3.IconButton(
                        onClick = { onImeSearch() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = OnSecondary
                        )
                    }
                },
                singleLine = true,
                keyboardActions = KeyboardActions(
                    onSearch = { onImeSearch() }
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotBlank(),
                        enter = scaleIn(initialScale = 0.5f) + fadeIn(),
                        exit = scaleOut(targetScale = 0.5f) + fadeOut()
                    ) {
                        androidx.compose.material3.IconButton(
                            onClick = {
                                onSearchQueryChange("")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.close_hint),
                                tint = OnSecondary
                            )
                        }
                    }
                },
                modifier = modifier
                    .background(
                        shape = RoundedCornerShape(100),
                        color = SearchBarBackground
                    )
                    .minimumInteractiveComponentSize()
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                items(10) { index ->
                    Text(
                        text = "Item $index",
                        color = OnSecondary
                    )
                }
            }

        }

    }


}