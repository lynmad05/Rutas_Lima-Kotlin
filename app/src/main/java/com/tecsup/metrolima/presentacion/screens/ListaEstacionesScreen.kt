package com.tecsup.metrolima.presentacion.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones
import com.tecsup.metrolima.ui.theme.*
import com.tecsup.metrolima.viewmodel.ListaEstacionesViewModel

data class Station(
    val id: Int,
    val name: String,
    val district: String,
    val imageUrl: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionScreen(
    navController: NavHostController,
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val viewModel: ListaEstacionesViewModel = viewModel(
        factory = ListaEstacionesViewModel.provideFactory(context)
    )

    val estaciones by viewModel.estaciones.collectAsState()

    // Convertir las estaciones de Room a tu modelo visual
    val stations = estaciones.map {
        Station(
            id = it.id,
            name = it.nombre,
            district = it.direccion,
            imageUrl = R.drawable.estacion_cultura
        )
    }

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
            // Campo de búsqueda (por ahora deshabilitado)
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Buscar estaciones o rutas") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { onSearchClick() },
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SearchBarBackground,
                        unfocusedContainerColor = SearchBarBackground,
                        disabledContainerColor = SearchBarBackground,
                        disabledBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledTextColor = SearchBarContentColor,
                        disabledLeadingIconColor = SearchBarContentColor,
                        disabledPlaceholderColor = SearchBarContentColor
                    )
                )
            }

            // Lista de estaciones
            items(stations) { station ->
                StationListItem(
                    station = station,
                    onClick = {
                        // Navegar al detalle de la estación
                        navController.navigate("detalle/${station.name}/${station.district}")
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
fun StationListItem(station: Station, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = station.imageUrl),
            contentDescription = station.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(90.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = station.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = StationNameColor
            )
            Text(
                text = station.district,
                style = MaterialTheme.typography.bodySmall,
                color = StationDescriptionColor
            )
        }

        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = "Ir a detalles de ${station.name}",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewListaEstacionScreen() {
    MetroLimaGoTheme {
        val navController = androidx.navigation.compose.rememberNavController()
        Surface(color = MaterialTheme.colorScheme.background) {
            ListaEstacionScreen(navController = navController)
        }
    }
}
