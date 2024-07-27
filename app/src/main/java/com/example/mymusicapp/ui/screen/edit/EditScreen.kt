package com.example.mymusicapp.ui.screen.edit

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.mymusicapp.R
import com.example.mymusicapp.ui.theme.Background
import com.example.mymusicapp.ui.theme.IconTintColor

@OptIn(UnstableApi::class)
@Preview
@Composable
fun EditScreen(
    viewModel: EditViewModel = viewModel(),
    navController: NavHostController = NavHostController(LocalContext.current),
) {

    viewModel.message.value.let {
        if (it.isNotEmpty()) {
            Toast.makeText(LocalContext.current, it, Toast.LENGTH_SHORT).show()
        }
    }

    val image: MutableState<Uri?> = remember {
        mutableStateOf(null)
    }
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { image.value = it }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        val iconSize = 40.dp
        IconButton(
            onClick = { /*TODO*/ }, modifier = Modifier
                .size(iconSize)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back), contentDescription = null,
                tint = IconTintColor, modifier = Modifier.fillMaxSize()
            )
        }
        MyTextField(labelString = "File Name", state = viewModel.fileName)
        MyTextField(labelString = "Title", state = viewModel.title)
        MyTextField(labelString = "Artist", state = viewModel.artist)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0xFF3D3D3C),
                            Color(0xFF2B2A29),
                            Color(0xFF3D3D3C),
                            Color(0xFF2B2A29),
                            Color(0xFF3D3D3C),
                            Color(0xFF2B2A29),
                        )
                    )
                )
        ) {
            viewModel.song.imageBitmap.let {
                if (it != null)
                    Image(
                        bitmap = it, contentDescription = null,
                        contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                    )
            }
            AsyncImage(
                model = image.value,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Button(
                onClick = { viewModel.saveSongFile(image.value) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF479a89)
                ), shape = RectangleShape, modifier = Modifier.weight(1f)
            ) {
                Text(text = "Save")
            }
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFfa6356)
                ), shape = RectangleShape, modifier = Modifier.weight(1f)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFe0f5ec)
@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    state: MutableState<String> = mutableStateOf(""),
    labelString: String = "Username"
) {

    TextField(
        singleLine = true,
        value = state.value,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFF479a89),
            focusedContainerColor = Color(0xFF479a89),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth(),
        label = {
            Text(
                text = labelString,
                modifier = Modifier.padding(start = 10.dp)
            )
        },
        shape = RectangleShape,
        onValueChange = {
            state.value = it
        }
    )
}
