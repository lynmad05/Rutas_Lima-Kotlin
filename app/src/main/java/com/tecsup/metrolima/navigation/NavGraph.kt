package com.tecsup.metrolima.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tecsup.metrolima.presentacion.screens.HomeScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // 🏠 Pantalla principal
        composable("home") {
            HomeScreen(navController = navController)
        }


        // 🚏 Pantalla de rutas (temporal)
        composable("rutas") {
            PlaceholderScreen("Pantalla de Rutas")
        }

        // 🚉 Lista de estaciones (tu pantalla con ViewModel)
        composable("listado") {
            ListaEstacionScreen(
                onStationClick = { /* Aquí luego navegarás a detalle */ }
            )
        }

        // 🚆 Pantalla del tren (temporal)
        composable("tren") {
            PlaceholderScreen("Pantalla del Tren")
        }

        // ⚙️ Configuración
        composable("configuracion") {
            PlaceholderScreen("Pantalla de Configuración")
        }
    }
}

@Composable
fun PlaceholderScreen(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}
