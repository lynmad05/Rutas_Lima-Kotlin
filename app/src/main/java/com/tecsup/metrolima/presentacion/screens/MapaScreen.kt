package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.maps.android.compose.*
import com.tecsup.metrolima.viewmodel.MapaLineaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaScreen(
    navController: NavHostController,
    lineaId: Int
) {
    val context = LocalContext.current
    val viewModel: MapaLineaViewModel = viewModel(
        factory = MapaLineaViewModel.provideFactory(context, lineaId)
    )
    val coordenadas by viewModel.coordenadasLinea.collectAsState()

    val colorLinea = when (lineaId) {
        1 -> Color(0xFF259AA8)
        2 -> Color(0xFF9C27B0)
        else -> Color(0xFF112180)
    }
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(coordenadas) {
        if (coordenadas.isNotEmpty()) {
            val builder = LatLngBounds.builder()
            coordenadas.forEach { builder.include(it) }
            val bounds = builder.build()
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        } else {
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(LatLng(-12.05, -77.03), 12f)
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Mapa de la Línea $lineaId", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = colorLinea.copy(alpha = 0.15f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        listOf(colorLinea.copy(alpha = 0.05f), Color.White)
                    )
                )
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    mapToolbarEnabled = false,
                    compassEnabled = true
                )
            ) {
                if (coordenadas.isNotEmpty()) {
                    Polyline(
                        points = coordenadas,
                        color = colorLinea,
                        width = 10f
                    )

                    coordenadas.forEachIndexed { index, coord ->
                        Marker(
                            state = MarkerState(position = coord),
                            title = "Estación ${index + 1}",
                            snippet = "Línea $lineaId"
                        )
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        if (coordenadas.isNotEmpty()) {
                            val builder = LatLngBounds.builder()
                            coordenadas.forEach { builder.include(it) }
                            val bounds = builder.build()
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngBounds(bounds, 100)
                            )
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                containerColor = colorLinea,
                contentColor = Color.White,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.MyLocation, contentDescription = "Centrar mapa")
            }
        }
    }
}
