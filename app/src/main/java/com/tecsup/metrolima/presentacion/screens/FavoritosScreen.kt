package com.tecsup.metrolima.presentacion.screens

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(navController: NavHostController) {
    val context = LocalContext.current
    val vm: RutaViewModel = viewModel(factory = RutaViewModel.provideFactory(context))
    val rutas by vm.savedRoutes.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Rutas Favoritas") }) }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            items(rutas) { r ->
                Text("Desde: ${r.nombreEstacionOrigen}")
                Text("Hasta: ${r.nombreEstacionDestino}")
                Text("Tiempo: ${r.tiempoEstimadoMinutos} min")
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}
