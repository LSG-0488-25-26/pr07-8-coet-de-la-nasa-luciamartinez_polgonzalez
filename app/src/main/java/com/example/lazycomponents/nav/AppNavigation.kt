package com.example.lazycomponents.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
    if (orientation == ScreenOrientation.PORTRAIT) {
        // Vertical: NavigationBar de Material3
        MobilePortraitLayout(viewModel, navController, padding)
    } else {
        // Horizontal: BottomBar personalizada
        MobileLandscapeLayout(viewModel, navController, padding)
    }
}

@Composable
fun MobilePortraitLayout(
    viewModel: KaraokeViewModel,
    navController: NavHostController,
    padding: Dp
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = currentRoute == Routes.HOME,
                    onClick = {
                        navController.navigate(Routes.HOME) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") },
                    selected = currentRoute == Routes.FAVORITES,
                    onClick = {
                        navController.navigate(Routes.FAVORITES) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
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
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun MobileLandscapeLayout(
    viewModel: KaraokeViewModel,
    navController: NavHostController,
    padding: Dp
) {
    Scaffold(
        bottomBar = {
            // BottomBar personalizada
            Surface(
                modifier = Modifier.height(56.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val textColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)

                    // Botones: Icono y texto en horizontal
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                navController.navigate(Routes.HOME) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentRoute == Routes.HOME) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .fillMaxHeight(0.7f)
                                    .aspectRatio(3.2f)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "Inicio",
                                modifier = Modifier.size(24.dp),
                                tint = textColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Inicio",
                                style = MaterialTheme.typography.labelMedium,
                                color = textColor
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable {
                                navController.navigate(Routes.FAVORITES) {
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentRoute == Routes.FAVORITES) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                    .fillMaxHeight(0.7f)
                                    .aspectRatio(3.2f)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Favoritos",
                                modifier = Modifier.size(24.dp),
                                tint = textColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Favoritos",
                                style = MaterialTheme.typography.labelMedium,
                                color = textColor
                            )
                        }
                    }
                }
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
                            restoreState = true
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
                        text = "Karaoke App",
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
                        text = "Favoritos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = getAdaptativeFontSize(getScreenSize(), 22).sp,
                        modifier = Modifier.padding(bottom = tabletPadding / 2)
                    )

                    FavoritesScreen(
                        viewModel = viewModel,
                        screenPadding = tabletPadding / 1.5f,
                        // Tablet → no necesita navegación
                        onSongClick = {}
                    )
                }
            }
        }
    }
}