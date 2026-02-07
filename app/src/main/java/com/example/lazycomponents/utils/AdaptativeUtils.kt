package com.example.lazycomponents.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class ScreenSize {
    SMALL,
    // Teléfonos grandes / tablets pequeñas
    MEDIUM,
    // Tablets grandes
    LARGE
}

@Composable
fun getScreenSize(): ScreenSize {
    val screenInfo = getScreenInfo()
    val smallestWidth = minOf(screenInfo.screenWidth, screenInfo.screenHeight)

    return when {
        smallestWidth < 600.dp -> ScreenSize.SMALL
        smallestWidth < 840.dp -> ScreenSize.MEDIUM
        else -> ScreenSize.LARGE
    }
}

fun getAdaptativePadding(screenSize: ScreenSize): Dp {
    return when (screenSize) {
        ScreenSize.SMALL -> 16.dp
        ScreenSize.MEDIUM -> 24.dp
        ScreenSize.LARGE -> 32.dp
    }
}

fun getAdaptativeFontSize(screenSize: ScreenSize, baseSize: Int): Int {
    return when (screenSize) {
        ScreenSize.SMALL -> baseSize
        ScreenSize.MEDIUM -> (baseSize * 1.1).toInt()
        ScreenSize.LARGE -> (baseSize * 1.2).toInt()
    }
}