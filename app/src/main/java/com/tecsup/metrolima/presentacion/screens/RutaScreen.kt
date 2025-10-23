package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.viewmodel.RutaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutaScreen(
    navController: NavHostController,
    viewModel: RutaViewModel,
    onMenuClick: () -> Unit = {},
) {
    val context = LocalContext.current

    // Cambié rutaviewModel por viewModel (el parámetro)
    val selectedTransportOption by viewModel.selectedTransportOption.collectAsState()
    val selectedOptimizationOption by viewModel.selectedOptimizationOption.collectAsState()

    val filteredOrigenes by viewModel.filteredOrigenes.collectAsState()
    val filteredDestinos by viewModel.filteredDestinos.collectAsState()
    val searchOrigenText by viewModel.searchOrigenText.collectAsState()
    val searchDestinoText by viewModel.searchDestinoText.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarEstaciones(
                title = "Planifica tu Ruta",
                onMenuClick = onMenuClick
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = searchOrigenText,
                onValueChange = { viewModel.onSearchOrigenChange(it) },
                label = { Text("¿Desde dónde?") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Icono de ubicación",
                        tint = Color(0xFF00BCD4)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = Color(0xFF00BCD4),
                    unfocusedLabelColor = Color(0xFF757575),
                    cursorColor = Color(0xFF00BCD4)
                )
            )

            // Lista de sugerencias de origen
            if (filteredOrigenes.isNotEmpty() && searchOrigenText.isNotBlank()){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ){
                    filteredOrigenes.forEach { estacion ->
                        Text(
                            text = estacion.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable{
                                    viewModel.onOrigenSelected(estacion)
                                },
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchDestinoText,
                onValueChange = { viewModel.onSearchDestinoChange(it) },
                label = { Text("¿Hacia dónde?") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Icono de ubicación",
                        tint = Color(0xFF00BCD4)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedLabelColor = Color(0xFF00BCD4),
                    unfocusedLabelColor = Color(0xFF757575),
                    cursorColor = Color(0xFF00BCD4)
                )
            )

            // Lista de sugerencias de destino
            if (filteredDestinos.isNotEmpty() && searchDestinoText.isNotBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    filteredDestinos.forEach { estacion ->
                        Text(
                            text = estacion.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    viewModel.onDestinoSelected(estacion)
                                },
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.onTransportOptionSelected("Metro") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTransportOption == "Metro") Color(0xFFE0BBE4) else Color(0xFFF0F0F0),
                        contentColor = if (selectedTransportOption == "Metro") Color(0xFF8D53A4) else Color(0xFF757575)

                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Metro", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = { viewModel.onOptimizationOptionSelected("Menos Transbordos") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOptimizationOption == "Menos Transbordos") Color(0xFFE0BBE4) else Color(0xFFF0F0F0),
                        contentColor = if (selectedOptimizationOption == "Menos Transbordos") Color(0xFF8D53A4) else Color(0xFF757575)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Text("Menos Transbordos", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { viewModel.onCalcularRutaClick() },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3AB1BD),
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "Calcular Ruta",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            val resultadoRuta by viewModel.resultadoRuta.collectAsState()

            Spacer(modifier = Modifier.height(24.dp))

            resultadoRuta?.let { resultado ->
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .background(Color.White)
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Tiempo grande en minutos
                    Text(
                        text = resultado.tiempoEstimado,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Origen y destino resumidos
                    val origen = viewModel.origenEstacion.value?.nombre ?: "?"
                    val destino = viewModel.destinoEstacion.value?.nombre ?: "?"

                    Text(
                        text = "Desde: $origen | Hasta: $destino",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF4FC3F7),
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Lista de pasos
                    resultado.estacionesIntermedias.forEachIndexed { index, estacion ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Ícono
                            Icon(
                                imageVector = when (index % 3) {
                                    0 -> Icons.Filled.LocationOn
                                    1 -> Icons.Filled.DirectionsTransit
                                    else -> Icons.Filled.DirectionsWalk
                                },
                                contentDescription = null,
                                tint = Color(0xFF006064),
                                modifier = Modifier.size(32.dp)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Tomar ${estacion.linea} en ${estacion.nombre}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = estacion.distrito,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            }
                        }
                    }

                    // Botón de iniciar ruta
                    Button(
                        onClick = { navController.navigate("iniciarRuta") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4FC3F7),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp)
                    ) {
                        Text("Iniciar Ruta", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewRutas() {
    MetroLimaGoTheme {
        RutaScreen(
            navController = null,
            onMenuClick = { /* Preview click */ }
        )
    }
}
*/