package com.example.lazycomponents.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.min

data class ScreenInfo(
    val screenWidth: Dp,
    val screenHeight: Dp,
    val screenWidthPx: Int,
    val screenHeightPx: Int,
    val density: Float,
    val orientation: ScreenOrientation
)

enum class ScreenOrientation {
    PORTRAIT,
    LANDSCAPE
}

@Composable
fun getScreenInfo(): ScreenInfo {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current.density

    return ScreenInfo(
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp,
        screenWidthPx = (configuration.screenWidthDp * density).toInt(),
        screenHeightPx = (configuration.screenHeightDp * density).toInt(),
        density = density,
        orientation = if (configuration.screenWidthDp < configuration.screenHeightDp)
            ScreenOrientation.PORTRAIT
        else
            ScreenOrientation.LANDSCAPE
    )
}

fun minOf(a: Dp, b: Dp): Dp {
    return if (a.value <= b.value) a else b
}