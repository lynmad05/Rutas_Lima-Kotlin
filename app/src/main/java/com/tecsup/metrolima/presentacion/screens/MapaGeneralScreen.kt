package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.viewmodel.MapaGeneralViewModel
import kotlinx.coroutines.launch

val Purple80 = Color(0xFF79B7BE)
val SelectedButtonColor = Color(0xFFD0BCFF)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaGeneralScreen(
    navController: NavController,
    viewModel: MapaGeneralViewModel = viewModel(factory = MapaGeneralViewModel.provideFactory(LocalContext.current))
) {
    val lineas = viewModel.lineas.collectAsState()
    val scope = rememberCoroutineScope()
    val initialPosition = LatLng(-12.05, -77.03)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 11f)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(navController) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mapa General del Metro de Lima",
                        color = Color.Black,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Purple80.copy(alpha = 0.9f)
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Purple80.copy(alpha = 0.15f),
                            SelectedButtonColor.copy(alpha = 0.25f)
                        )
                    )
                )
                .padding(paddingValues)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState
                    ) {
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

                    LegendBox(
                        lineas = lineas.value,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                    )

                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(initialPosition, 11f)
                                )
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        containerColor = SelectedButtonColor,
                        contentColor = Color.Black,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "Centrar mapa")
                    }
                }
            }
        }
    }
}


@Composable
fun LegendBox(lineas: List<MapaGeneralViewModel.LineaConPuntos>, modifier: Modifier = Modifier) {
    if (lineas.isNotEmpty()) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(16.dp),
            shadowElevation = 6.dp,
            color = Color.White.copy(alpha = 0.95f)
        ) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(lineas.size) { index ->
                    val linea = lineas[index]
                    LegendChip(nombre = linea.nombre, color = Color(linea.color))
                }
            }
        }
    }
}


@Composable
fun LegendChip(nombre: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50),
        color = color.copy(alpha = 0.15f),
        tonalElevation = 3.dp,
        modifier = Modifier
            .border(1.dp, color.copy(alpha = 0.7f), RoundedCornerShape(50))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = nombre,
                color = Color.Black,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
