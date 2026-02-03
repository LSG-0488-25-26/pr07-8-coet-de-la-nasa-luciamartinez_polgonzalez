package com.example.lazycomponents.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.lazycomponents.view.FavoritesScreen
import com.example.lazycomponents.view.HomeScreen
import com.example.lazycomponents.viewmodel.KaraokeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(viewModel: KaraokeViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Star, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") },
                    selected = currentRoute == "favorites",
                    onClick = { navController.navigate("favorites") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                HomeScreen(viewModel)
            }
            composable("favorites") {
                FavoritesScreen(viewModel) {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
    }
}