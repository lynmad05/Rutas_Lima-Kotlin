package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
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
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.ui.theme.CardDescriptionColor
import com.tecsup.metrolima.ui.theme.ChipSelectedColor
import com.tecsup.metrolima.ui.theme.ChipUnselectedContentColor
import com.tecsup.metrolima.ui.theme.OnChipSelectedColor
import com.tecsup.metrolima.ui.theme.SearchBarBackground
import com.tecsup.metrolima.ui.theme.SearchBarContentColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: androidx.navigation.NavHostController,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onItemClick = { item ->
                    scope.launch { drawerState.close() }
                    when (item) {
                        "Inicio" -> navController.navigate("home")
                        "Mis rutas" -> navController.navigate("rutas")
                        "Favoritos" -> navController.navigate("favoritos")
                        "Configuración" -> navController.navigate("configuracion")
                        "Cerrar sesión" -> { /* lógica para cerrar sesión */ }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithMenuAndNotifications(
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onNotificationsClick = { /* Navegar a notificaciones */ }
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
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { Text("Buscar estaciones o rutas") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { navController.navigate("listado") },
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

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = true,
                            enabled = true,
                            onClick = { },
                            label = { Text("Mis Favoritos") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ChipSelectedColor,
                                selectedLabelColor = OnChipSelectedColor,
                                selectedLeadingIconColor = OnChipSelectedColor,
                                containerColor = Color.Transparent,
                                labelColor = ChipUnselectedContentColor
                            ),
                        )
                        FilterChip(
                            selected = true,
                            enabled = true,
                            onClick = { },
                            label = { Text("Últimas Rutas") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ChipSelectedColor,
                                selectedLabelColor = OnChipSelectedColor,
                                selectedLeadingIconColor = OnChipSelectedColor,
                                containerColor = Color.Transparent,
                                labelColor = ChipUnselectedContentColor
                            ),
                        )
                        FilterChip(
                            selected = true,
                            enabled = true,
                            onClick = { },
                            label = { Text("Líneas/Mapas") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ChipSelectedColor,
                                selectedLabelColor = OnChipSelectedColor,
                                selectedLeadingIconColor = OnChipSelectedColor,
                                containerColor = Color.Transparent,
                                labelColor = ChipUnselectedContentColor
                            ),
                        )
                    }
                }

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
                        item {
                            PopularPlaceCard(
                                title = "Ruta",
                                subtitulo = "Estación Central a Miraflores",
                                description = "Línea 1",
                                imageUrl = R.drawable.estacion_central
                            )
                        }
                        item {
                            PopularPlaceCard(
                                title = "Estación",
                                subtitulo = "Estación Central",
                                description = "Cercado de Lima",
                                imageUrl = R.drawable.cercado
                            )
                        }
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
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.Search, contentDescription = "Ver detalles")
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
                            painter = painterResource(id = R.drawable.proximamente),
                            contentDescription = "Tren en construcción",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerContent(onItemClick: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .wrapContentHeight()
            .width(240.dp)
            .padding(vertical = 24.dp)
            .clip(RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)),
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = Color(0xFFF7F7F7)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Menú",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DrawerItem("Inicio", onItemClick)
            DrawerItem("Mis rutas", onItemClick)
            DrawerItem("Favoritos", onItemClick)
            DrawerItem("Configuración", onItemClick)
            DrawerItem("Cerrar sesión", onItemClick)
        }
    }
}


@Composable
fun DrawerItem(text: String, onItemClick: (String) -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(text) }
            .padding(vertical = 12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithMenuAndNotifications(
    onMenuClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        title   = { Text("MetroLima GO") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menú")
            }
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones")
            }
        }
    )
}

@Composable
fun PopularPlaceCard(title: String, subtitulo: String, description: String, imageUrl: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(180.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
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
                    text = subtitulo,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = CardDescriptionColor
                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewHomeScreen() {
    MetroLimaGoTheme {
        HomeScreen(navController = androidx.navigation.compose.rememberNavController())
    }
}
