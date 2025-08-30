package nd.phuc.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nd.phuc.core.presentation.previews.DefaultPreview

enum class AppButtonSize { Small, Medium, Large }


@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: AppButtonSize = AppButtonSize.Medium
) {

//    val (height, fontSize, horizontalPadding, verticalPadding) = when (size) {
//        AppButtonSize.Small -> Quadruple(36.dp, 14.sp, 16.dp, 6.dp)
//        AppButtonSize.Medium -> Quadruple(48.dp, 16.sp, 20.dp, 10.dp)
//        AppButtonSize.Large -> Quadruple(56.dp, 18.sp, 24.dp, 14.dp)
//    }
//
//    Button(
//        onClick = onClick,
//        enabled = enabled,
//        modifier = modifier
//            .height(height)
//            .padding(horizontal = 0.dp, vertical = 0.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = MaterialTheme.colorScheme.primary,
//            contentColor = MaterialTheme.colorScheme.onPrimary,
//            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
//        ),
//        shape = MaterialTheme.shapes.medium,
//        contentPadding = ButtonDefaults.ContentPadding.copy(
//            start = horizontalPadding,
//            end = horizontalPadding,
//            top = verticalPadding,
//            bottom = verticalPadding
//        )
//    ) {
//        Text(
//            text = text,
//            fontSize = fontSize,
//            fontWeight = FontWeight.Bold
//        )
//    }
}

@Composable
fun ErrorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: AppButtonSize = AppButtonSize.Medium
) {
//    val (height, fontSize, horizontalPadding, verticalPadding) = when (size) {
//        AppButtonSize.Small -> Quadruple(36.dp, 14.sp, 16.dp, 6.dp)
//        AppButtonSize.Medium -> Quadruple(48.dp, 16.sp, 20.dp, 10.dp)
//        AppButtonSize.Large -> Quadruple(56.dp, 18.sp, 24.dp, 14.dp)
//    }
//
//    Button(
//        onClick = onClick,
//        enabled = enabled,
//        modifier = modifier
//            .height(height)
//            .padding(horizontal = 0.dp, vertical = 0.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = MaterialTheme.colorScheme.errorContainer,
//            contentColor = MaterialTheme.colorScheme.onErrorContainer,
//            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
//        ),
//        shape = MaterialTheme.shapes.medium,
//        contentPadding = ButtonDefaults.ContentPadding.copy(
//            start = horizontalPadding,
//            end = horizontalPadding,
//            top = verticalPadding,
//            bottom = verticalPadding
//        )
//    ) {
//        Text(
//            text = text,
//            fontSize = fontSize,
//            fontWeight = FontWeight.Bold
//        )
//    }
}

@Composable
@DefaultPreview
fun PreviewAppButtons() {
//    MyMusicAppTheme {
//        Column(modifier = Modifier.padding(16.dp)) {
//            AppButton(text = "Small Button", onClick = {}, size = AppButtonSize.Small)
//            Spacer(modifier = Modifier.height(8.dp))
//            AppButton(text = "Medium Button", onClick = {}, size = AppButtonSize.Medium)
//            Spacer(modifier = Modifier.height(8.dp))
//            AppButton(text = "Large Button", onClick = {}, size = AppButtonSize.Large)
//        }
//    }
}

@Composable
@DefaultPreview
fun PreviewErrorButtons() {
//    MyMusicAppTheme {
//        Column(modifier = Modifier.padding(16.dp)) {
//            ErrorButton(text = "Small Error", onClick = {}, size = AppButtonSize.Small)
//            Spacer(modifier = Modifier.height(8.dp))
//            ErrorButton(text = "Medium Error", onClick = {}, size = AppButtonSize.Medium)
//            Spacer(modifier = Modifier.height(8.dp))
//            ErrorButton(text = "Large Error", onClick = {}, size = AppButtonSize.Large)
//        }
//    }
}
