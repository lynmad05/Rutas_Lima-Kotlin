package com.tecsup.metrolima.presentacion.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.metrolima.viewmodel.RutaViewModel

@SuppressLint("UnrememberedGetBackStackEntry", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarRutaScreen(navController: NavHostController) {
    val context = LocalContext.current
    val parentEntry = remember(navController) { navController.getBackStackEntry("rutas") }
    val viewModel: RutaViewModel = viewModel(parentEntry, factory = RutaViewModel.provideFactory(context))

    val origen by viewModel.origenEstacion.collectAsState()
    val destino by viewModel.destinoEstacion.collectAsState()
    val resultadoRuta by viewModel.resultadoRuta.collectAsState()
    val isFav by viewModel.isCurrentRouteFavorite.collectAsState()

    // --- CONFIGURACIÓN DE MAPA ---
    val origenLatLng = origen?.let { LatLng(it.latitud, it.longitud) }
    val destinoLatLng = destino?.let { LatLng(it.latitud, it.longitud) }

    // Calcula el punto medio entre las estaciones para enfocar mejor el mapa
    val centerLatLng = remember(origenLatLng, destinoLatLng) {
        if (origenLatLng != null && destinoLatLng != null) {
            LatLng(
                (origenLatLng.latitude + destinoLatLng.latitude) / 2,
                (origenLatLng.longitude + destinoLatLng.longitude) / 2
            )
        } else {
            LatLng(-12.0464, -77.0428) // Lima centro por defecto
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(centerLatLng, 13f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MetroLima GO") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    TextButton(onClick = { navController.navigate("rutas") }) {
                        Text("Finalizar", color = Color(0xFF00BCD4))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---  MAPA  ---
            if (origenLatLng != null && destinoLatLng != null) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .shadow(6.dp, RoundedCornerShape(20.dp)),
                    cameraPositionState = cameraPositionState
                ) {
                    Marker(
                        state = MarkerState(position = origenLatLng),
                        title = origen!!.nombre,
                        snippet = "Origen"
                    )
                    Marker(
                        state = MarkerState(position = destinoLatLng),
                        title = destino!!.nombre,
                        snippet = "Destino"
                    )

                    // Simulación de trazo curvo estilo "ruta del metro"
                    val puntos = listOf(
                        origenLatLng,
                        LatLng(
                            (origenLatLng.latitude + destinoLatLng.latitude) / 2 + 0.005,
                            (origenLatLng.longitude + destinoLatLng.longitude) / 2
                        ),
                        destinoLatLng
                    )

                    Polyline(
                        points = puntos,
                        color = Color(0xFF0091EA),
                        width = 12f
                    )
                }
            } else {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Selecciona origen y destino para ver el mapa.")
                }
            }

            // ---  CARD  ---
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(12.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FBFC)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    Modifier
                        .padding(vertical = 16.dp, horizontal = 20.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Desde: ${origen?.nombre ?: "Selecciona origen"}",
                        fontSize = 17.sp,
                        color = Color(0xFF1565C0),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Hasta: ${destino?.nombre ?: "Selecciona destino"}",
                        fontSize = 17.sp,
                        color = Color(0xFF00897B),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    resultadoRuta?.let {
                        Text(
                            "Duración: ${it.tiempoEstimado}",
                            fontSize = 15.sp,
                            color = Color(0xFF424242),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // --- BOTÓN FAVORITO ---
            Button(
                onClick = { viewModel.saveCurrentRoute() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFav) Color(0xFFFFCDD2) else Color(0xFFE0F7FA),
                    contentColor = if (isFav) Color(0xFFB71C1C) else Color.Black
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(55.dp)
                    .shadow(4.dp, RoundedCornerShape(14.dp))
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito"
                )
                Spacer(Modifier.width(8.dp))
                Text(if (isFav) "Ruta guardada" else "Guardar como favorita")
            }

            Spacer(Modifier.height(20.dp))

            //  BOTÓN FAVORITOS ---
            Button(
                onClick = { navController.navigate("favoritos") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A237E),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text("Ver Favoritos", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
