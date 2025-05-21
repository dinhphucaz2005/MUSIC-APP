package com.example.musicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Custom color scheme
val customDarkColorScheme = darkColorScheme(
    primary = Color(0xFF171C26),              // Nền chính đậm
    onPrimary = Color.White,                  // Chữ trên primary

    secondary = Color(0xFF6C7689),            // Màu phụ
    onSecondary = Color.White,

    tertiary = Color(0xFFA4AAB7),             // Màu nhấn thứ ba
    onTertiary = Color.Black,

    background = Color(0xFF171C26),           // Nền toàn app
    onBackground = Color.White,               // Text trên nền tối

    surface = Color(0xFF1E1E1E),              // Nền khối UI
    onSurface = Color.White,

    surfaceVariant = Color(0xFF2E2E2E),       // Cho card, list, sheet
    onSurfaceVariant = Color(0xFFCCCCCC),

    surfaceTint = Color(0xFF171C26),          // Màu dùng làm tint cho elevation

    inverseSurface = Color.White,
    inverseOnSurface = Color(0xFF171C26),

    inversePrimary = Color(0xFF9BB3FF),

    error = Color(0xFFCF6679),                // Màu lỗi nền tối
    onError = Color.Black,

    errorContainer = Color(0xFFB00020),
    onErrorContainer = Color.White,

    primaryContainer = Color(0xFF2C313D),     // Dùng cho nút nổi
    onPrimaryContainer = Color.White,

    secondaryContainer = Color(0xFF444B5A),
    onSecondaryContainer = Color.White,

    tertiaryContainer = Color(0xFFBEC3D1),
    onTertiaryContainer = Color.Black,

    outline = Color(0xFF8E8E93),
    outlineVariant = Color(0xFF2C2C2E),

    scrim = Color.Black.copy(alpha = 0.5f)
)


@Composable
fun MyMusicAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = customDarkColorScheme,
        typography = Typography,
        content = content
    )
}