package nd.phuc.musicapp.music.presentation.ui.feature.playlist.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import nd.phuc.musicapp.core.presentation.components.AppOutlinedTextField

@Composable
fun FastCreatePlaylistDialog(
    onDismiss: () -> Unit,
    onCreatePlaylist: (name: String) -> Unit
) {
    var playlistName by remember { mutableStateOf("") }
    var isNameError by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Create Playlist",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppOutlinedTextField(
                    value = playlistName,
                    onValueChange = { 
                        playlistName = it
                        isNameError = false
                    },
                    label = { Text("Playlist Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isNameError,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (playlistName.isNotBlank()) {
                                onCreatePlaylist(playlistName.trim())
                            } else {
                                isNameError = true
                            }
                        }
                    ),
                    supportingText = if (isNameError) {
                        { Text("Playlist name cannot be empty") }
                    } else null
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (playlistName.isBlank()) {
                        isNameError = true
                    } else {
                        onCreatePlaylist(playlistName.trim())
                    }
                }
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        textContentColor = MaterialTheme.colorScheme.onBackground
    )
}