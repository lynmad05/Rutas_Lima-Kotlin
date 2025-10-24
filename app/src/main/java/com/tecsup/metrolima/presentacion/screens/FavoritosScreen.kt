package com.tecsup.metrolima.presentacion.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import com.tecsup.metrolima.viewmodel.RutaViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedGetBackStackEntry")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(navController: NavHostController) {
    val context = LocalContext.current
    val parentEntry = remember(navController) { navController.getBackStackEntry("rutas") }
    val viewModel: RutaViewModel = viewModel(parentEntry, factory = RutaViewModel.provideFactory(context))
    val rutas by viewModel.savedRoutes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rutas Favoritas") },
                navigationIcon = {
                    IconButton({ navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (rutas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Aún no tienes rutas guardadas")
            }
        } else {
            LazyColumn(Modifier.padding(padding).padding(16.dp)) {
                items(rutas) { ruta ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Desde: ${ruta.nombreEstacionOrigen}", fontWeight = FontWeight.Bold)
                            Text("Hasta: ${ruta.nombreEstacionDestino}", color = Color.Gray)
                            Text("Tiempo: ${ruta.tiempoEstimadoMinutos} min")
                        }
                    }
                }
            }
        }
    }
}

