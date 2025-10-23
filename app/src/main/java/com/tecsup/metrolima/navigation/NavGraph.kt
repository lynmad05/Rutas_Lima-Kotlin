package com.tecsup.metrolima.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.presentacion.screens.DetalleEstacionScreen
import com.tecsup.metrolima.presentacion.screens.FavoritosScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import kotlinx.coroutines.delay
import com.tecsup.metrolima.presentacion.screens.HomeScreen
import com.tecsup.metrolima.presentacion.screens.IniciarRutaScreen
import com.tecsup.metrolima.presentacion.screens.RutaScreen
import com.tecsup.metrolima.presentacion.screens.SplashScreen
import com.tecsup.metrolima.viewmodel.RutaViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("favoritos") {
            FavoritosScreen(navController = navController)
        }

        composable("iniciarRuta") {
            val context = LocalContext.current
            val viewModel: RutaViewModel = viewModel(
                factory = RutaViewModel.provideFactory(context)
            )
            IniciarRutaScreen(navController = navController, viewModel = viewModel)
        }

        composable("rutas") {
            RutaScreen(navController = navController)
        }


        composable("mapa") {
            Text("Mapa interactivo (en desarrollo)", color = Color.Black)
        }
        composable("listado") {
            ListaEstacionScreen(navController = navController)
        }

        composable("config") {
            Text("Pantalla de configuración", color = Color.Black)
        }



        composable(
            route = "detalle/{estacionJson}",
            arguments = listOf(navArgument("estacionJson") { type = NavType.StringType })
        ) { backStackEntry ->
            // ... (tu lógica para DetalleEstacionScreen)
        }

        composable(
            route = "detalle/{estacionJson}",
            arguments = listOf(navArgument("estacionJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val estacionJson = backStackEntry.arguments?.getString("estacionJson")
            val estacion = estacionJson?.let { Gson().fromJson(it, Estacion::class.java) }

            if (estacion != null) {
                DetalleEstacionScreen(
                    navController = navController,
                    estacion = estacion
                )
            } else {
                Text(
                    "Error: Estación no encontrada. Volviendo...",
                    color = Color.Red
                )
                LaunchedEffect(Unit) {
                    delay(1500)
                    navController.popBackStack()
                }
            }
        }



    }
}