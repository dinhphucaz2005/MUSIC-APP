package com.example.mymusicapp.ui.screen.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun CalendarScreen() {

}


@Preview(showSystemUi = true, showBackground = true, backgroundColor = 0xFF2ea6e0)
@Composable
fun MyCalendar(
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
) {
    Column(
        modifier = modifier.wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            Text(
                text = "January 1970", modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null
                )
            }


        }

        val array = listOf(
            listOf("", "", "", "1", "2", "3", "4"),
            listOf("5", "6", "7", "8", "9", "10", "11"),
            listOf("12", "13", "14", "15", "16", "17", "18"),
            listOf("19", "20", "21", "22", "23", "24", "25"),
            listOf("26", "27", "28", "29", "30", "31", ""),
        )

        val list = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0..6) {
                Text(
                    text = list[i],
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center, fontSize = 16.sp
                )
            }
        }
        array.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp)
            ) {
                for (i in 0..6) {
                    Text(
                        text = row[i],
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold, fontSize = 16.sp
                    )
                }
            }
        }
    }
}