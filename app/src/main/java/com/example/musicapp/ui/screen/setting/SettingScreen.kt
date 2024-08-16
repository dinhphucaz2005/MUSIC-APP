@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.musicapp.ui.screen.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.ui.AppViewModel
import com.example.musicapp.di.FakeModule
import com.example.musicapp.ui.theme.MusicTheme
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker

@UnstableApi
@Composable
fun SettingScreen(
    appViewModel: AppViewModel
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        var color by remember { mutableStateOf(HsvColor(0f, 0f, 1f, 1f)) }
        var boxColor by remember { mutableStateOf(color.toColor()) }
        var value by remember { mutableFloatStateOf(1f) }

        val colorPicker = createRef()
        HarmonyColorPicker(
            harmonyMode = ColorHarmonyMode.NONE,
            color = color,
            showBrightnessBar = false,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(colorPicker) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
        ) {
            color = it
            value = 1f
            boxColor = color.toColor()
        }


        Box(
            modifier = Modifier
                .border(width = 5.dp, color = Color.Black)
                .size(100.dp)
                .background(color = boxColor)
        )

        Slider(value = value, onValueChangeFinished = {
        }, onValueChange = {
            value = it
            val tmp = color.toColor()
            val red = tmp.red * value
            val green = tmp.green * value
            val blue = tmp.blue * value
            boxColor = Color(red, green, blue)
        })
        Row(
            modifier = Modifier.constrainAs(createRef()) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
            },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                },
            ) {
                Text(text = "Set background color")
            }
        }

    }
}