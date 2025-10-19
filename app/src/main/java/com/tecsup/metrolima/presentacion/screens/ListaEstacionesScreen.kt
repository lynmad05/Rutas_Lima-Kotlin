package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.ui.theme.SearchBarBackground
import com.tecsup.metrolima.ui.theme.SearchBarContentColor

import com.tecsup.metrolima.ui.theme.StationDescriptionColor
import com.tecsup.metrolima.ui.theme.StationNameColor
import com.tecsup.metrolima.ui.theme.DividerColor


data class Station(
    val id: Int,
    val name: String,
    val district: String,
    val imageUrl: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionScreen(
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onStationClick: (Station) -> Unit = {}
) {

    val stations = listOf(
        Station(1, "Estación La Cultura", "San Borja", R.drawable.estacion_cultura),
        Station(2, "Estación Bayóvar", "San Juan de Lurigancho", R.drawable.estacion_bayovar),
        Station(3, "Estación Grau", "Cercado de Lima", R.drawable.proximamente),
        Station(4, "Estación Gamarra", "La Victoria", R.drawable.gamarra),
        Station(5, "Estación Ricardo Palma", "Miraflores", R.drawable.ricardo_palma),
        Station(6, "Estación Angamos", "Surquillo", R.drawable.angamos),
        Station(7, "Estación Villa el Salvador", "Villa El Salvador", R.drawable.estacion_cultura)
    )

    Scaffold(
        topBar = {
            TopAppBarEstaciones(
                onMenuClick = onMenuClick,
                onNotificationsClick = onNotificationsClick
            )
        },
        bottomBar = { BottomNavigationBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* onSearchTextChanged(it) */ },
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

            items(stations) { station ->
                StationListItem(station = station, onClick = { onStationClick(station) })
                Divider( // Línea divisoria entre ítems
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

        Column(
            modifier = Modifier.weight(1f)
        ) {
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

        Spacer(modifier = Modifier.width(16.dp))
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
        Surface(color = MaterialTheme.colorScheme.background) {
            ListaEstacionScreen()
        }
    }
}