/*
package nd.phuc.musicapp.music.presentation.ui.feature.playlist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import coil3.compose.AsyncImage
import nd.phuc.core.presentation.components.AppOutlinedTextField
import nd.phuc.musicapp.music.presentation.ui.feature.playlist.CreatePlaylistViewModel
import nd.phuc.musicapp.music.presentation.ui.feature.playlist.PlaylistRepository
import nd.phuc.musicapp.music.presentation.ui.feature.playlist.dialog.FastCreatePlaylistDialog
import nd.phuc.core.presentation.theme.MyMusicAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlaylistScreen(
    viewModel: CreatePlaylistViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onPlaylistCreatedSuccessfully: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showThumbnailDialog by remember { mutableStateOf(false) }
    var showFastCreatePlaylistDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.creationError) {
        viewModel.creationError?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(viewModel.creationSuccess) {
        if (viewModel.creationSuccess) {
            onPlaylistCreatedSuccessfully()
        }
    }

    if (showThumbnailDialog) {
        ThumbnailSelectionDialog(
            onDismiss = { showThumbnailDialog = false },
            onThumbnailSelected = { url ->
                viewModel.onThumbnailSelected(url)
                showThumbnailDialog = false
            }
        )
    }

    if (showFastCreatePlaylistDialog) {
        FastCreatePlaylistDialog(
            onDismiss = { showFastCreatePlaylistDialog = false },
            onCreatePlaylist = { playlistName ->
                viewModel.onPlaylistNameChange(playlistName)
                viewModel.createPlaylist()
                showFastCreatePlaylistDialog = false
            },
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Create New Playlist") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                ThumbnailSelector(
                    modifier = Modifier
                        .size(200.dp)
                        .aspectRatio(1f),
                    selectedThumbnailUrl = viewModel.thumbnailUrl,
                    onClick = { showThumbnailDialog = true }
                )

                Button(onClick = { showFastCreatePlaylistDialog = true }) {
                    Text("Fast Create Playlist")
                }

                AppOutlinedTextField(
                    value = viewModel.playlistName,
                    onValueChange = viewModel::onPlaylistNameChange,
                    label = { Text("Playlist Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = viewModel.creationError != null && viewModel.playlistName.isBlank(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                )

                AppOutlinedTextField(
                    value = viewModel.playlistDescription,
                    onValueChange = viewModel::onPlaylistDescriptionChange,
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            if (!viewModel.isLoading) {
                                viewModel.createPlaylist()
                            }
                        }
                    ),
                )

                Spacer(modifier = Modifier.weight(1f))

                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.createPlaylist()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !viewModel.isLoading
                    ) {
                        Text("Create Playlist")
                    }
                }

                Spacer(modifier = Modifier.height(80.dp)) // Bottom spacing for player
            }
        }
    }
}

@Composable
fun ThumbnailSelector(
    modifier: Modifier = Modifier,
    selectedThumbnailUrl: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondary)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selectedThumbnailUrl.isNullOrEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Select cover image",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Choose Cover",
                    color = MaterialTheme.colorScheme.tertiary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            AsyncImage(
                model = selectedThumbnailUrl,
                contentDescription = "Selected thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ThumbnailSelectionDialog(
    onDismiss: () -> Unit,
    onThumbnailSelected: (String) -> Unit
) {
    val thumbnailOptions = remember {
        listOf(
            "https://i.scdn.co/image/ab67616d0000b273c9ef3e01dab0f72488b8fa88",
            "https://i.scdn.co/image/ab67616d0000b273f317c63a4c3ced9d2e926e8d",
            "https://i.scdn.co/image/ab67616d0000b2734c79d5ec52a6d0302f3add25",
            "https://i.scdn.co/image/ab67616d0000b273ad2f6c9ee2ebb5578beb9090",
            "https://i.scdn.co/image/ab67616d0000b273e8b066f70c206551210d902b",
            "https://i.scdn.co/image/ab67616d0000b27320e08c8cc23f404d723b5647",
            "https://i.scdn.co/image/ab67616d0000b273ba5db46f4b838ef6027e6f96",
            "https://i.scdn.co/image/ab67616d0000b2738b52c6b9bc4e43d873869699"
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Select Cover Image",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column {
                    Text(
                        text = "Choose a thumbnail for your playlist",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(thumbnailOptions) { thumbnailUrl ->
                            ThumbnailOption(
                                thumbnailUrl = thumbnailUrl,
                                onClick = { onThumbnailSelected(thumbnailUrl) }
                            )
                        }

                        // Add option for custom image (could be implemented later)
                        item {
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.secondary)
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { */
/* Upload custom image *//*
 },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Upload custom image",
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            textContentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ThumbnailOption(
    thumbnailUrl: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = "Thumbnail option",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Add a subtle overlay for better text visibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.1f))
        )

        // Selection indicator could be added here
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Select thumbnail",
            tint = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(4.dp)
                .size(24.dp)
        )
    }
}

@Preview
@Composable
private fun CreatePlaylistScreenPreview() {
    MyMusicAppTheme {
        val previewViewModel = remember {
            CreatePlaylistViewModel(
                playlistRepository = PlaylistRepository(),
                savedStateHandle = SavedStateHandle()
            ).apply {
                // Set some initial values for the preview
                onPlaylistNameChange("My Awesome Playlist")
                onPlaylistDescriptionChange("A collection of my favorite tracks")
            }
        }

        CreatePlaylistScreen(
            viewModel = previewViewModel,
            onNavigateBack = {},
            onPlaylistCreatedSuccessfully = {}
        )
    }
}*/
