package com.example.kharjyar.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

private val LightColors = lightColorScheme(
    primary = Color(0xFF0F766E),
    secondary = Color(0xFFF59E0B),
    background = Color(0xFFF4F7F6),
    surface = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF2DD4BF),
    secondary = Color(0xFFFBBF24),
    background = Color(0xFF101514),
    surface = Color(0xFF1A2120)
)

val IncomeColor = Color(0xFF16A34A)
val ExpenseColor = Color(0xFFDC2626)

// رنگ‌های نمودار دایره‌ای
val chartColors = listOf(
    Color(0xFF0F766E),
    Color(0xFFF59E0B),
    Color(0xFF6366F1),
    Color(0xFFEC4899),
    Color(0xFF3B82F6),
    Color(0xFF84CC16),
    Color(0xFF94A3B8)
)

@Composable
fun KharjyarTheme(darkTheme: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (darkTheme) DarkColors else LightColors) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            content()
        }
    }
}
