package com.example.lazycomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lazycomponents.nav.Routes
import com.example.lazycomponents.ui.theme.LazyComponentsTheme
import com.example.lazycomponents.view.PantallaDetallsGat
import com.example.lazycomponents.view.PantallaLlistaGats
import com.example.lazycomponents.viewmodel.GatViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: GatViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            NavegacionApp(viewModel)
        }
    }
}

@Composable
fun NavegacionApp(viewModel: GatViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Llista.route
    ) {
        composable(Routes.Llista.route) {
            PantallaLlistaGats(
                viewModel = viewModel,
                alNavegarDetalle = { id ->
                    navController.navigate(Routes.Detalls.createRoute(id))
                }
            )
        }

        composable(
            route = Routes.Detalls.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            PantallaDetallsGat(
                viewModel = viewModel,
                gatoId = id,
                alVolver = { navController.popBackStack() }
            )
        }
    }
}
