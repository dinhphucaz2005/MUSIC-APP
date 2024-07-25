package com.example.mymusicapp.ui.screen.authentication.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.mymusicapp.R

@Preview
@Composable
fun LoginScreen() {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.image2),
            modifier = Modifier.fillMaxSize(), contentDescription = null
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(
                        width = 5.dp,
                        color = Color.White,
                        shape = CircleShape
                    ),
                contentDescription = null
            )
            TextField(value = "", onValueChange = {})
            TextField(value = "", onValueChange = {})
            Button(onClick = { /*TODO*/ }) {
                Text(text = "          Login          ")
            }
            Text(text = "          Login          ")
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.facebook),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }
}

