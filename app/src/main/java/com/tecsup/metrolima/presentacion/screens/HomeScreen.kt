package com.tecsup.metrolima.presentacion.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.metrolima.R
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarWithMenuAndNotifications
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},

) {
    Scaffold(
        topBar = {
            TopAppBarWithMenuAndNotifications(
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
                Text(
                    text = "MetroLima GO",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )


                OutlinedTextField(
                    value = "",
                    onValueChange = { /* */ },
                    placeholder = { Text("Buscar estaciones o rutas") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { onSearchClick() },
                    enabled = false,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        disabledBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    )
                )

                //Las categorias
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(selected = true, onClick = { /* Filtro Favoritos */ }, label = { Text("Mis Favoritos") })
                    FilterChip(selected = false, onClick = { /* Filtro Últimas Rutas */ }, label = { Text("Últimas Rutas") })
                    FilterChip(selected = false, onClick = { /* Filtro Líneas y Mapas */ }, label = { Text("Líneas y Mapas") })
                }
            }

            // Sección "Lugares Populares" (Tarjetas con imágenes)
            item {
                Text(
                    text = "Lugares Populares",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Aquí deberías iterar sobre una lista de "PopularPlace" o "SuggestedRoute"
                    // Por ahora, usamos placeholders:
                    item {
                        PopularPlaceCard(
                            title = "Ruta: Miraflores",
                            description = "Estación Central a Miraflores",
                            imageUrl =R.drawable.estacion_central
                        )
                    }
                    item {
                        PopularPlaceCard(
                            title = "Estación Central",
                            description = "Cercado de Lima",
                            imageUrl =R.drawable.cercado
                        )
                    }
                    // Agrega más tarjetas si lo deseas
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Próximamente",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = { /* Navegar a pantalla de detalles de líneas en desarrollo */ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Ver detalles") // Placeholder
                    }
                }

                Text(
                    text = "Más líneas en camino",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                )
                Text(
                    text = "Líneas 2, 3, 4 y 6 en desarrollo",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Image(
                        painter = painterResource(id =  R.drawable.proximamente),
                        contentDescription = "Tren en construcción",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}


@Composable
fun PopularPlaceCard(title: String, description: String, imageUrl: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            // Aquí el icono de corazón lo pones dentro de la imagen o en un Box superpuesto para que se vea como en la referencia
            // O puedes dejarlo al final de la columna como ahora si te gusta más
            Icon(
                Icons.Filled.FavoriteBorder,
                contentDescription = "Favorito",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(bottom = 4.dp, end = 4.dp) // Ajusta si lo mueves de lugar
                    .size(24.dp)
            )
        }
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewHomeScreen() {
    MetroLimaGoTheme {
        HomeScreen()
    }
}