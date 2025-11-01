package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tecsup.metrolima.viewmodel.ListaEstacionesViewModel
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones
import com.tecsup.metrolima.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstacionesPorLineaScreen(
    navController: NavHostController,
    lineaId: Int,
    viewModel: ListaEstacionesViewModel = viewModel(factory = ListaEstacionesViewModel.provideFactory(LocalContext.current))
) {
    val estaciones by viewModel.estacionesFiltradas.collectAsState()
    val context = LocalContext.current

    // Filtrar estaciones por línea
    LaunchedEffect(lineaId) {
        val nombreLinea = when (lineaId) {
            1 -> "Línea 1 Metro"
            2 -> "Línea 2 Metro"
            else -> "Línea 1 Metro"
        }
        viewModel.filtrarPorLinea(nombreLinea)
    }


    Scaffold(
        topBar = { TopAppBarEstaciones(title = "Estaciones de la Línea") },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (estaciones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay estaciones registradas para esta línea.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(estaciones) { estacion ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // En el futuro: ver detalle estación
                                },
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = estacion.nombre,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = estacion.distrito,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
