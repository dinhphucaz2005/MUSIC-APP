package com.example.musicapp.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Preview
@Composable
private fun Preview() {
    Test()
}

@Composable
fun Test() {
    Box {
        CoilImage(
            imageModel = { "https://raw.githubusercontent.com/coredxor/images/main/bk_login.png" },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Logo()
            Text(
                "Login",
                color = Color(0xFF181725),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                "Enter your emails and password",
                color = Color(0xFF7C7C7C),
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )
            Email()
            Password()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                ) {
                }
                TextButton(
                    onClick = {},
                ) {
                    Text(
                        "Forgot Password?",
                        color = Color(0xFF181725),
                        fontSize = 14.sp,
                    )
                }
            }
            ButtonLogin()
            CanSignup()
        }
    }
}


@Composable
fun Logo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(50.dp)
            .fillMaxWidth()
    ) {
        CoilImage(
            imageModel = { "https://raw.githubusercontent.com/coredxor/images/main/carot_login.png" },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
        )
    }
}

@Composable
fun Email() {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Email",
            color = Color(0xFF7C7C7C),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        BasicTextField(
            value = "redxor@gmail.com",
            onValueChange = { },
            textStyle = TextStyle(
                color = Color(0xFF181725),
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .padding(vertical = 12.dp)
                .height(22.dp)
                .fillMaxWidth()
                .background(Color(0xFFFFFFFF))
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color(0xFFE2E2E2))
        ) {
        }
    }
}

@Composable
fun Password() {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            "Password",
            color = Color(0xFF7C7C7C),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        BasicTextField(
            value = "*************",
            onValueChange = { },
            textStyle = TextStyle(
                color = Color(0xFF181725),
                fontSize = 18.sp,
            ),
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp)
                .height(22.dp)
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFFFFFF),
                )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(
                    color = Color(0xFFE2E2E2),
                )
        ) {
        }
    }
}

@Composable
fun ButtonLogin() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .clip(shape = RoundedCornerShape(20.dp))
            .height(65.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xFF53B175),
                shape = RoundedCornerShape(20.dp)
            )
    ) {
        Text(
            "LOG IN",
            color = Color(0xFFFFF9FF),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun CanSignup() {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            "Don’t have an account?",
            color = Color(0xFF181725),
            fontSize = 14.sp,
        )
        TextButton(
            onClick = {},
        ) {
            Text(
                " Signup",
                color = Color(0xFF53B175),
                fontSize = 14.sp,
            )
        }
    }
}
