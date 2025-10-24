package com.tecsup.metrolima.presentacion.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutaScreen(
    navController: NavHostController,
    onMenuClick: () -> Unit = {},
) {
    val context = LocalContext.current

    // ✅ use remember to prevent recomposition issue
    val parentEntry = remember(navController) { navController.getBackStackEntry("rutas") }
    val viewModel: RutaViewModel = viewModel(
        parentEntry,
        factory = RutaViewModel.provideFactory(context)
    )

    // ✅ State collectors
    val selectedTransportOption: String by viewModel.selectedTransportOption.collectAsState()
    val selectedOptimizationOption: String by viewModel.selectedOptimizationOption.collectAsState()
    val filteredOrigenes by viewModel.filteredOrigenes.collectAsState()
    val filteredDestinos by viewModel.filteredDestinos.collectAsState()
    val searchOrigenText by viewModel.searchOrigenText.collectAsState()
    val searchDestinoText by viewModel.searchDestinoText.collectAsState()
    val resultadoRuta by viewModel.resultadoRuta.collectAsState()

    Scaffold(
        topBar = {
            TopAppBarEstaciones(
                title = "Planifica tu Ruta",
                onMenuClick = onMenuClick
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // -------------------- CAMPO ORIGEN --------------------
            OutlinedTextField(
                value = searchOrigenText,
                onValueChange = { viewModel.onSearchOrigenChange(it) },
                label = { Text("¿Desde dónde?") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFF00BCD4))
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00BCD4),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

            if (filteredOrigenes.isNotEmpty() && searchOrigenText.isNotBlank()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                        .padding(8.dp)
                ) {
                    filteredOrigenes.forEach { estacion ->
                        Text(
                            text = estacion.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.onOrigenSelected(estacion) }
                                .padding(8.dp),
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // -------------------- CAMPO DESTINO --------------------
            OutlinedTextField(
                value = searchDestinoText,
                onValueChange = { viewModel.onSearchDestinoChange(it) },
                label = { Text("¿Hacia dónde?") },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                trailingIcon = {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Color(0xFF00BCD4))
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF00BCD4),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )

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
                                .clickable { viewModel.onDestinoSelected(estacion) }
                                .padding(8.dp),
                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // -------------------- OPCIONES --------------------
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.onTransportOptionSelected("Metro") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTransportOption == "Metro") Color(0xFFE0BBE4) else Color(0xFFF0F0F0),
                        contentColor = if (selectedTransportOption == "Metro") Color(0xFF8D53A4) else Color(0xFF757575)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Metro")
                }

                Spacer(modifier = Modifier.size(16.dp))

                Button(
                    onClick = { viewModel.onOptimizationOptionSelected("Menos Transbordos") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedOptimizationOption == "Menos Transbordos") Color(0xFFE0BBE4) else Color(0xFFF0F0F0),
                        contentColor = if (selectedOptimizationOption == "Menos Transbordos") Color(0xFF8D53A4) else Color(0xFF757575)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Menos Transbordos")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // -------------------- CALCULAR RUTA --------------------
            Button(
                onClick = { viewModel.onCalcularRutaClick() },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3AB1BD))
            ) {
                Text("Calcular Ruta", fontWeight = FontWeight.Bold)
            }

            // -------------------- RESULTADO --------------------
            resultadoRuta?.let { resultado ->
                Spacer(modifier = Modifier.height(32.dp))
                val origen = viewModel.origenEstacion.value?.nombre ?: ""
                val destino = viewModel.destinoEstacion.value?.nombre ?: ""

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Tiempo estimado: ${resultado.tiempoEstimado}",
                        fontWeight = FontWeight.Bold
                    )
                    Text("Desde: $origen  →  Hasta: $destino", color = Color(0xFF4FC3F7))

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { navController.navigate("iniciarRuta") },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4FC3F7))
                    ) {
                        Text("Iniciar Ruta", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRutaScreen() {
    MetroLimaGoTheme {
        RutaScreen(navController = rememberNavController())
    }
}
