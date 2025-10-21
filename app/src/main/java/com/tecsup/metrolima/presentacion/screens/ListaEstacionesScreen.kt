package com.tecsup.metrolima.presentacion.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.tecsup.metrolima.R
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones
import com.tecsup.metrolima.ui.theme.*
import com.tecsup.metrolima.viewmodel.ListaEstacionesViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionScreen(
    navController: NavHostController,
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: ListaEstacionesViewModel = viewModel(
        factory = ListaEstacionesViewModel.provideFactory(context)
    )

    val estaciones by viewModel.estacionesFiltradas.collectAsState()


    Scaffold(
        topBar = {
            TopAppBarEstaciones(
                onMenuClick = onMenuClick,
                onNotificationsClick = onNotificationsClick
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                val searchText by viewModel.searchText.collectAsState()

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { viewModel.onSearchTextChange(it) },
                    placeholder = { Text(text = "Buscar estaciones o rutas") },
                    leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    enabled = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SearchBarBackground,
                        unfocusedContainerColor = SearchBarBackground,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = SearchBarContentColor,
                        unfocusedTextColor = SearchBarContentColor,
                        focusedLeadingIconColor = SearchBarContentColor,
                        unfocusedLeadingIconColor = SearchBarContentColor,
                        focusedPlaceholderColor = SearchBarContentColor,
                        unfocusedPlaceholderColor = SearchBarContentColor
                    )
                )
            }



            items(estaciones) { estacion ->
                StationListItem(
                    estacion = estacion,
                    onClick = {
                        val estacionJson = Uri.encode(Gson().toJson(estacion))
                        navController.navigate("detalle/${estacionJson}")
                    }
                )
                Divider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = DividerColor
                )
            }
        }
    }
}

@Composable
fun StationListItem(estacion: Estacion, onClick: () -> Unit, modifier: Modifier = Modifier) { // Recibe Estacion
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = estacion.imagenCircularResId), // Usamos imagenCircularResId
            contentDescription = estacion.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(90.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = estacion.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = StationNameColor
            )
            Text(
                text = estacion.distrito, // Usamos distrito
                style = MaterialTheme.typography.bodySmall,
                color = StationDescriptionColor
            )
        }

        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = "Ir a detalles de ${estacion.nombre}",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewListaEstacionScreen() {
    MetroLimaGoTheme {
        val navController = rememberNavController()
        Surface(color = MaterialTheme.colorScheme.background) {
            ListaEstacionScreen(navController = navController)
        }
    }
}