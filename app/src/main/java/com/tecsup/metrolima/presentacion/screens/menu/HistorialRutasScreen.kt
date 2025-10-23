package com.tecsup.metrolima.presentacion.screens.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun HistorialRutasScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            com.tecsup.metrolima.ui.components.TopAppBarWithMenuAndNotifications(
                onMenuClick = { navController.popBackStack() },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Pantalla: Historial de Rutas")
        }
    }
}