package com.tecsup.metrolima.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tecsup.metrolima.presentacion.screens.HomeScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import com.tecsup.metrolima.presentacion.screens.DetalleEstacionScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // 🏠 Pantalla principal (Home)
        composable("home") {
            HomeScreen(navController = navController)
        }

        // 🚉 Lista de estaciones
        composable("listado") {
            ListaEstacionScreen(navController = navController)
        }

        // ℹ️ Detalle de estación (recibe nombre y dirección)
        composable(
            route = "detalle/{nombre}/{direccion}"
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val direccion = backStackEntry.arguments?.getString("direccion") ?: ""

            DetalleEstacionScreen(
                navController = navController,
                nombre = nombre,
                direccion = direccion
            )
        }
    }
}
