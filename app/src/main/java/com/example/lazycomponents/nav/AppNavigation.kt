package com.example.lazycomponents.nav

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lazycomponents.utils.ScreenOrientation
import com.example.lazycomponents.utils.ScreenSize
import com.example.lazycomponents.utils.getAdaptativeFontSize
import com.example.lazycomponents.utils.getAdaptativePadding
import com.example.lazycomponents.utils.getScreenInfo
import com.example.lazycomponents.utils.getScreenSize
import com.example.lazycomponents.views.FavoritesScreen
import com.example.lazycomponents.views.HomeScreen
import com.example.lazycomponents.viewmodel.KaraokeViewModel

// Rutas para evitar líos
private object Routes {
    const val HOME = "home"
    const val FAVORITES = "favorites"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: KaraokeViewModel) {
    val navController = rememberNavController()
    val screenInfo = getScreenInfo()
    val screenSize = getScreenSize()
    val paddingValue = getAdaptativePadding(screenSize)

    // Solo es tablet si LARGE y LANDSCAPE
    val isRealTablet = screenSize == ScreenSize.LARGE &&
            screenInfo.orientation == ScreenOrientation.LANDSCAPE &&
            screenInfo.screenWidth > 800.dp

    if (isRealTablet) {
        TabletLayout(viewModel, paddingValue)
    } else {
        // Teléfonos: vertical y horizontal
        MobileLayout(viewModel, navController, paddingValue, screenInfo.orientation)
    }
}

@Composable
fun MobileLayout(
    viewModel: KaraokeViewModel,
    navController: NavHostController,
    padding: Dp,
    orientation: ScreenOrientation
) {
    Scaffold(
        bottomBar = {
            // Horizontal: bottomBar compacta
            if (orientation == ScreenOrientation.PORTRAIT) {
                NavigationBar {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                        label = {
                            Text(
                                "Inicio",
                                fontSize = getAdaptativeFontSize(getScreenSize(), 12).sp
                            )
                        },
                        selected = currentRoute == Routes.HOME,
                        onClick = {
                            navController.navigate(Routes.HOME) {
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Star, contentDescription = "Favoritos") },
                        label = {
                            Text(
                                "Favoritos",
                                fontSize = getAdaptativeFontSize(getScreenSize(), 12).sp
                            )
                        },
                        selected = currentRoute == Routes.FAVORITES,
                        onClick = {
                            navController.navigate(Routes.FAVORITES) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    screenPadding = padding
                )
            }
            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    viewModel = viewModel,
                    screenPadding = padding,
                    onSongClick = {
                        navController.navigate(Routes.HOME) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TabletLayout(
    viewModel: KaraokeViewModel,
    padding: Dp
) {
    // Tablet: reducir padding
    val tabletPadding = padding * 0.7f

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        // Columna izquierda: Home 60%
        Column(
            modifier = Modifier
                .weight(0.6f)
                .fillMaxHeight()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(tabletPadding),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(tabletPadding)
                ) {
                    Text(
                        text = "🎤 Karaoke App",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = getAdaptativeFontSize(getScreenSize(), 22).sp,
                        modifier = Modifier.padding(bottom = tabletPadding / 2)
                    )

                    HomeScreen(
                        viewModel = viewModel,
                        screenPadding = tabletPadding / 1.5f
                    )
                }
            }
        }

        // Columna derecha: Favoritos 40%
        Column(
            modifier = Modifier
                .weight(0.4f)
                .fillMaxHeight()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(tabletPadding),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(tabletPadding)
                ) {
                    Text(
                        text = "⭐ Favoritos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = getAdaptativeFontSize(getScreenSize(), 22).sp,
                        modifier = Modifier.padding(bottom = tabletPadding / 2)
                    )

                    FavoritesScreen(
                        viewModel = viewModel,
                        screenPadding = tabletPadding / 1.5f,
                        //Tablet --> no nav.
                        onSongClick = {}
                    )
                }
            }
        }
    }
}