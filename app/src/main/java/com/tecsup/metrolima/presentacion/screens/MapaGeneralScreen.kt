package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.tecsup.metrolima.viewmodel.MapaGeneralViewModel

@Composable
fun MapaGeneralScreen(
    navController: NavController,
    viewModel: MapaGeneralViewModel = viewModel(factory = MapaGeneralViewModel.provideFactory(LocalContext.current))
) {
    val lineas = viewModel.lineas.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.05, -77.03), 11f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Dibujamos cada línea separada
        lineas.value.forEach { linea ->
            if (linea.puntos.isNotEmpty()) {
                Polyline(
                    points = linea.puntos,
                    color = Color(linea.color),
                    width = 8f,
                    geodesic = true
                )
            }
        }
    }
}
