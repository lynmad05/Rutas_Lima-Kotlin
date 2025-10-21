package com.tecsup.metrolima.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.presentacion.screens.DetalleEstacionScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import kotlinx.coroutines.delay
import android.net.Uri // Importa Uri para el encoding del JSON en la navegación
import com.tecsup.metrolima.presentacion.screens.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home" // <--- ¡Asegúrate de que sea "home" aquí!
    ) {
        composable("home") {
            // Aquí vamos a usar un Composable dedicado para Home, no solo un Text
            HomeScreen(navController = navController) // <--- ¡CAMBIO AQUÍ!
        }

        composable("rutas") {
            Text("Pantalla de rutas", color = Color.Black)
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