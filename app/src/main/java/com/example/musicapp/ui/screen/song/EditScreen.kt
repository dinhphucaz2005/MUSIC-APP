package com.example.musicapp.ui.screen.song

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import coil.compose.AsyncImage
import com.example.musicapp.di.FakeModule
import com.example.musicapp.domain.model.Song
import com.example.musicapp.extension.getFileNameWithoutExtension
import com.example.musicapp.ui.theme.MusicTheme
import com.example.musicapp.ui.theme.commonShape

@Preview
@Composable
fun EditScreenPreview() {
    MusicTheme {
        EditScreen(song = Song(), onDismiss = {}, viewModel = FakeModule.provideEditViewModel())
    }
}

@OptIn(UnstableApi::class)
@Composable
fun EditScreen(
    song: Song?,
    onDismiss: () -> Unit = {},
    viewModel: EditViewModel = hiltViewModel(),
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { if (it != null) imageUri = it }
    )

    var fileName by remember { mutableStateOf(song?.fileName?.getFileNameWithoutExtension() ?: "") }
    var title by remember { mutableStateOf(song?.title ?: "") }
    var artist by remember { mutableStateOf(song?.author ?: "") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        item {
            Text(
                text = "Tag editor",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
        item {
            MyTextField(
                label = "File Name",
                value = fileName,
                onValueChange = { fileName = it })
        }
        item {
            MyTextField(label = "Title", value = title, onValueChange = {
                title = it
            })
        }
        item {
            MyTextField(label = "Artist", value = artist, onValueChange = {
                artist = it
            })
        }
        item {
            ImageSelector(
                imageUri = imageUri,
                song = song,
                photoPickerLauncher = photoPickerLauncher
            ) {
                imageUri = null
            }
        }
        item {
            ActionButtons(
                onSaveClick = {
                    song?.let {
                        viewModel.saveSongFile(
                            fileName = fileName,
                            title = title,
                            artist = artist,
                            imageUri = imageUri,
                            song = it
                        ) {
                            onDismiss()
                        }
                    }
                },
                onCancelClick = onDismiss
            )
        }
    }
}

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
) {
    OutlinedTextField(
        singleLine = true,
        value = value,
        onValueChange = { onValueChange(it) },
        label = { Text(text = label) },
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary
        ),
        shape = commonShape,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun ImageSelector(
    imageUri: Uri?,
    song: Song?,
    photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(commonShape)
            .aspectRatio(1f)
            .clickable {
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            .background(
                color = if (song?.smallBitmap == null && imageUri == null)
                    MaterialTheme.colorScheme.tertiary else Color.Transparent
            )
    ) {
        when {
            imageUri != null -> {
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
                IconButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            song?.smallBitmap != null -> {
                Image(
                    bitmap = song.smallBitmap,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

            else -> {
                Text(
                    text = "Select Image",
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

    }
}

@Composable
fun ActionButtons(
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Button(
            onClick = onSaveClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ),
            shape = commonShape,
            modifier = Modifier.weight(1f)
        ) {
            Text(text = "Save", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = onCancelClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shape = commonShape,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Cancel",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}
