package nd.phuc.musicapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout

@OptIn(ExperimentalMotionApi::class, ExperimentalMotionApi::class)
@Composable
fun MotionBottomSheetScreen() {
    val progress = remember { mutableFloatStateOf(0f) } // 0f = collapsed, 1f = expanded

    MotionLayout(
        start = ConstraintSet(
            """
            {
              bottomSheet: {
                width: 'spread',
                height: 100,
                bottom: ['parent', 'bottom', 0]
              },
              fab: {
                width: 56, height: 56,
                bottom: ['bottomSheet', 'top', 16],
                end: ['parent', 'end', 16],
                alpha: 1
              },
              navBar: {
                width: 'spread',
                height: 80,
                bottom: ['parent','bottom',0]
              }
            }
        """
        ),
        end = ConstraintSet(
            """
            {
              bottomSheet: {
                width: 'spread',
                height: 'spread',   // full screen
                top: ['parent', 'top', 0]
              },
              fab: {
                width: 56, height: 56,
                bottom: ['navBar','top',16],
                end: ['parent','end',16],
                translationX: 200,   // trượt sang phải
                alpha: 0
              },
              navBar: {
                width: 'spread',
                height: 80,
                bottom: ['parent','bottom',0]
              }
            }
        """
        ),
        progress = progress.floatValue,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .layoutId("bottomSheet")
                .background(Color.Gray)
        ) {
            Text("Bottom Sheet Content")
        }

        Box(
            modifier = Modifier
                .layoutId("navBar")
                .background(Color.DarkGray)
        ) {
            Text("Navigation Bar", color = Color.White)
        }

        FloatingActionButton(
            onClick = {
                progress.floatValue = if (progress.floatValue == 1f) 0f else 1f
            },
            modifier = Modifier.layoutId("fab")
        ) {
            Text("Expand")
        }
    }
}
