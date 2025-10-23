package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tecsup.metrolima.R
import com.tecsup.metrolima.viewmodel.RutaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IniciarRutaScreen(navController: NavHostController, viewModel: RutaViewModel) {


    val resultadoRuta by viewModel.resultadoRuta.collectAsState()
    val origen = viewModel.origenEstacion.collectAsState().value
    val destino = viewModel.destinoEstacion.collectAsState().value

    // Aquí agregas este bloque para imprimir el estado actual
    LaunchedEffect(origen, destino) {
        println("🟢 Origen recibido: ${origen?.nombre}")
        println("🟢 Destino recibido: ${destino?.nombre}")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MetroLima GO") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
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

            // Imagen del mapa o ilustración
            Image(
                painter = painterResource(id = R.drawable.mapa_linea1),
                contentDescription = "Mapa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
            )

            // Datos de la ruta
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
                        text = origen?.nombre ?: "Sin origen",
                        color = if (origen == null) Color.Red else Color.Black,
                        fontWeight = if (origen == null) FontWeight.Bold else FontWeight.Normal
                    )


                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = destino?.nombre ?: "Sin destino",
                        color = if (destino == null) Color.Red else Color.Black,
                        fontWeight = if (destino == null) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Botón para guardar favorito
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
                    contentDescription = "Favorito",
                    tint = if (guardado) Color(0xFFB71C1C) else Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (guardado) "Ruta guardada" else "Guardar como ruta favorita")
            }

            Spacer(modifier = Modifier.height(16.dp))

            resultadoRuta?.let { resultado ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Tiempo estimado: ${resultado.tiempoEstimado}",
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estaciones intermedias: ${resultado.estacionesIntermedias.size}"
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (resultado.estacionesIntermedias.isNotEmpty()) {
                            Text(
                                text = "Próximo paso: ${resultado.estacionesIntermedias.first().nombre}"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {


                Button(
                    onClick = { navController.navigate("favoritos") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8BBD0),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver Favoritos")
                }
            }
        }
    }
}
