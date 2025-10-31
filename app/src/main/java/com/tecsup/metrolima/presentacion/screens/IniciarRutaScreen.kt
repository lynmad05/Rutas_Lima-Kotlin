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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.tecsup.metrolima.R
import com.tecsup.metrolima.viewmodel.RutaViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarRutaScreen(navController: NavHostController, viewModel: RutaViewModel) {

    val resultadoRuta by viewModel.resultadoRuta.collectAsState()
    val origen = viewModel.origenEstacion.collectAsState().value
    val destino = viewModel.destinoEstacion.collectAsState().value

    val origenLatLng = origen?.let { LatLng(it.lat, it.lon) }
    val destinoLatLng = destino?.let { LatLng(it.lat, it.lon) }

    //  Centrar mapa entre ambas estaciones
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            origenLatLng ?: LatLng(-12.0464, -77.0428),
            12f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = {
                        // Limpia el estado antes de volver
                        viewModel.clearRuta()
                        viewModel.clearBusqueda()

                        // Regresa y limpia el historial de navegación (para refrescar)
                        navController.navigate("rutas") {
                            popUpTo("rutas") { inclusive = true }
                            launchSingleTop = true
                        }
                    }) {
                        Text(stringResource(R.string.finalizar), color = Color(0xFF00BCD4))
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (origenLatLng != null && destinoLatLng != null) {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(12.dp)
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    cameraPositionState = cameraPositionState
                ) {
                    // Marcadores
                    Marker(
                        state = MarkerState(position = origenLatLng),
                        title = origen.nombre,
                        snippet = "Origen"
                    )
                    Marker(
                        state = MarkerState(position = destinoLatLng),
                        title = destino.nombre,
                        snippet = "Destino"
                    )

                    // Línea azul simulando la ruta del metro
                    val rutaSimulada = listOf(
                        origenLatLng,
                        LatLng(
                            (origenLatLng.latitude + destinoLatLng.latitude) / 2 + 0.003,
                            (origenLatLng.longitude + destinoLatLng.longitude) / 2
                        ),
                        destinoLatLng
                    )

                    Polyline(
                        points = rutaSimulada,
                        color = Color(0xFF0091EA),
                        width = 12f
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.mapa_linea1_desc))
                }
            }

            //  Card con los datos
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = origen?.nombre ?: stringResource(R.string.sin_origen),
                        color = if (origen == null) Color.Red else Color.Black,
                        fontWeight = if (origen == null) FontWeight.Bold else FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = destino?.nombre ?: stringResource(R.string.sin_destino),
                        color = if (destino == null) Color.Red else Color.Black,
                        fontWeight = if (destino == null) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            //  Botón de favorito
            var guardado by remember { mutableStateOf(false) }

            Button(
                onClick = {
                    viewModel.saveCurrentRoute()
                    guardado = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (guardado) Color(0xFFFFCDD2) else Color(0xFFE0F7FA),
                    contentColor = if (guardado) Color(0xFFB71C1C) else Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
            ) {
                Icon(
                    imageVector = if (guardado) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = stringResource(R.string.favorito),
                    tint = if (guardado) Color(0xFFB71C1C) else Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (guardado)
                        stringResource(R.string.ruta_guardada)
                    else
                        stringResource(R.string.guardar_favorito)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            resultadoRuta?.let { resultado ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.tiempo_estimado_label, resultado.tiempoEstimado),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.estaciones_intermedias_label, resultado.estacionesIntermedias.size)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (resultado.estacionesIntermedias.isNotEmpty()) {
                            Text(
                                text = stringResource(R.string.proximo_paso_label, resultado.estacionesIntermedias.first().nombre)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { navController.navigate("favoritos") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF8BBD0),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Text(stringResource(R.string.ver_favoritos))
            }
        }
    }
}
