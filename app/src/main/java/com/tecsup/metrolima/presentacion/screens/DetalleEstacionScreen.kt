package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Train
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.viewmodel.DetalleEstacionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleEstacionScreen(
    navController: NavController,
    estacionId: Int
) {
    val context = LocalContext.current

    val viewModel: DetalleEstacionViewModel = viewModel(
        factory = DetalleEstacionViewModel.provideFactory(context, estacionId)
    )

    val estacion by viewModel.estacion.collectAsState()
    val servicios by viewModel.serviciosCercanos.collectAsState()

    if (estacion == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.detalle_estacion_titulo)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        return
    }

    val estacionData = estacion!!

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = estacionData.nombre,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorite() }) {
                        Icon(
                            imageVector = if (estacionData.is_favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.favorito),
                            tint = if (estacionData.is_favorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Cámara
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    LatLng(estacionData.lat, estacionData.lon), 16f
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    // El Marcador
                    val estacionMarker = rememberMarkerState(
                        position = LatLng(estacionData.lat, estacionData.lon)
                    )
                    Marker(
                        state = estacionMarker,
                        title = estacionData.nombre,
                        snippet = estacionData.distrito
                    )

                    // Servicios cercanos de ejemplo
                    servicios.forEachIndexed { index, servicio ->
                        val serviceState = rememberMarkerState(
                            position = LatLng(
                                estacionData.lat + 0.0008 * (index + 1),
                                estacionData.lon + 0.0008 * (index + 1)
                            )
                        )
                        Marker(
                            state = serviceState,
                            title = servicio,
                            snippet = "Servicio cerca"
                        )
                    }
                }
            }

            Text(
                text = estacionData.nombre,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val tipoRuta = when (estacionData.linea_id) {
                        1 -> "Línea 1"
                        2 -> "Línea 2"
                        3 -> "Corredor Azul"
                        else -> "Ruta"
                    }

                    InfoCard(
                        label = "Tipo de Ruta",
                        value = tipoRuta,
                        icon = Icons.Default.Train,
                        modifier = Modifier.weight(1f)
                    )

                    InfoCard(
                        label = stringResource(R.string.distrito),
                        // 🟢 CAMBIO: Usar la data real
                        value = estacionData.distrito,
                        icon = Icons.Default.LocationOn,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoCard(
                        label = stringResource(R.string.coordenadas),
                        value = "${estacionData.lat}, ${estacionData.lon}",
                        icon = Icons.Default.Route,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        label = stringResource(R.string.horario),
                        value = estacionData.horario,
                        icon = Icons.Default.AccessTime,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = stringResource(R.string.servicios),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))


                servicios.forEach { servicio ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Train,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                servicio,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Button(
                    onClick = { navController.navigate("rutas") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9EE2F0)),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(stringResource(R.string.planificar_ruta), color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InfoCard(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE9D8FF)),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .padding(6.dp)
            .height(120.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.Black)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = label, fontWeight = FontWeight.SemiBold)
            Text(text = value, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}