package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.tecsup.metrolima.viewmodel.MapaGeneralViewModel

@Composable
fun MapaGeneralScreen(
    navController: NavController
    // Livia deberá añadir aquí el viewModel
    // viewModel: MapaGeneralViewModel
) {
    // Este Composable será el encargado de mostrar las 3 líneas (L1, L2, L3) a la vez. trazadas en el mapa.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Livia implementará aquí el Google Map
        Text("Mapa General - Trazando TODAS las Líneas")
        // Aquí se usará el ViewModel para obtener y dibujar las PolyLines de todas las líneas.
        // Ejemplo: GoogleMap { ... Polyline(puntos de L1), Polyline(puntos de L2) ... }
    }
}