package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.background
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
import com.tecsup.metrolima.viewmodel.ListaLineasViewModel
import com.tecsup.metrolima.data.model.Linea
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaLineasScreen(
    navController: NavHostController,
    viewModel: ListaLineasViewModel = viewModel(factory = ListaLineasViewModel.provideFactory(LocalContext.current))
) {
    val lineas by viewModel.lineas.collectAsState()

    Scaffold(
        topBar = { TopAppBarEstaciones(title = "Líneas del Metro") },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(12.dp)
        ) {
            items(lineas) { linea ->
                LineaCard(
                    linea = linea,
                    onVerEstacionesClick = {
                        navController.navigate("estaciones/linea/${linea.id}")
                    },
                    onVerMapaClick = {
                        navController.navigate("mapa_linea/${linea.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun LineaCard(
    linea: Linea,
    onVerEstacionesClick: () -> Unit,
    onVerMapaClick: () -> Unit
) {
    val color = try {
        Color(android.graphics.Color.parseColor(linea.color))
    } catch (e: Exception) {
        Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Información de la Línea
                Column {
                    Text(
                        text = linea.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Estado: ${linea.estado}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(color, shape = RoundedCornerShape(8.dp))
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onVerEstacionesClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Ver Estaciones")
                }

                Button(
                    onClick = onVerMapaClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Ver Mapa")
                }
            }
        }
    }
}