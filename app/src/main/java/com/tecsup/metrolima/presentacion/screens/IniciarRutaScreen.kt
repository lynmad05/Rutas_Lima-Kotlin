package com.tecsup.metrolima.presentacion.screens

import android.annotation.SuppressLint
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

@SuppressLint("UnrememberedGetBackStackEntry")
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
            Image(
                painter = painterResource(id = R.drawable.mapa_linea1),
                contentDescription = "Mapa",
                modifier = Modifier.fillMaxWidth().height(250.dp).padding(16.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Desde: ${origen?.nombre ?: "Selecciona origen"}")
                    Text("Hasta: ${destino?.nombre ?: "Selecciona destino"}")
                    resultadoRuta?.let {
                        Text("Duración: ${it.tiempoEstimado}")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveCurrentRoute() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFav) Color(0xFFFFCDD2) else Color(0xFFE0F7FA),
                    contentColor = if (isFav) Color(0xFFB71C1C) else Color.Black
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.9f).height(50.dp)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito"
                )
                Spacer(Modifier.width(8.dp))
                Text(if (isFav) "Ruta guardada" else "Guardar como favorita")
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = { navController.navigate("favoritos") }) {
                Text("Ver Favoritos")
            }
        }
    }
}