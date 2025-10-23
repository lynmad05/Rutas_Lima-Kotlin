package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    onMenuClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val viewModel: RutaViewModel = viewModel(
        factory = RutaViewModel.provideFactory(context)
    )

    var selectedTransportOption by remember { mutableStateOf("Metro") }
    var selectedOptimizationOption by remember { mutableStateOf("Menos Transbordos") }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = searchOrigenText,
                onValueChange = { viewModel.onSearchOrigenChange(it)},
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
                    focusedBorderColor = Color(0xFF00BCD4),
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
                onValueChange = { viewModel.onSearchDestinoChange(it)},
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
                    focusedBorderColor = Color(0xFF00BCD4),
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
                    onClick = { selectedTransportOption = "Metro" },
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
                    onClick = { selectedOptimizationOption = "Menos Transbordos" },
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
                onClick = { /* Lógica de cálculo de ruta */ },
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

        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewRutas() {
    MetroLimaGoTheme {
        RutaScreen(
            navController = rememberNavController(),
            onMenuClick = { /* Preview click */ }
        )
    }
}