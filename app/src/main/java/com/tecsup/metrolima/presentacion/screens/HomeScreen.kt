package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.filled.Restore
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarWithMenuAndNotifications
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.ui.theme.CardDescriptionColor
import com.tecsup.metrolima.ui.theme.SearchBarBackground
import com.tecsup.metrolima.ui.theme.SearchBarContentColor
import kotlinx.coroutines.launch

data class MenuDrawerItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

val menuItems = listOf(
    MenuDrawerItem("Mis rutas favoritas", Icons.Default.Star, "favoritos"),
    MenuDrawerItem("Historial de Rutas", Icons.Default.Restore, "historial_rutas"),
    MenuDrawerItem("Líneas y mapas", Icons.Default.Map, "lineas_mapas"),
    MenuDrawerItem("Acerca de la App", Icons.Default.Info, "acerca_app"),
)

@Composable
fun DrawerMenuItem(
    item: MenuDrawerItem,
    isSelected: Boolean,
    onItemClick: (String) -> Unit,
    isInfoItem: Boolean = false
) {
    val backgroundColor = if (isSelected) Color(0xFFE0F7FA) else Color.White
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface

    val infoBackgroundColor = if (isInfoItem) Color(0xFFF0F0F0) else backgroundColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(infoBackgroundColor)
            .clickable(enabled = !isInfoItem) {
                if (!isInfoItem && item.route.isNotEmpty()) {
                    onItemClick(item.route)
                }
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isInfoItem) Icons.AutoMirrored.Filled.Article else item.icon,
            contentDescription = item.title,
            tint = if (isInfoItem) MaterialTheme.colorScheme.onSurfaceVariant else contentColor
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (isInfoItem) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun DrawerContent(
    currentRoute: String,
    onItemClick: (String) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(280.dp),
        drawerContainerColor = Color.White
    ) {
        // Título del Menú
        Text(
            text = "Menú",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(20.dp)
        )
        Spacer(Modifier.height(8.dp))

        // Ítems de navegación
        menuItems.forEach { item ->
            val isSelected = currentRoute == item.route

            DrawerMenuItem(
                item = item,
                isSelected = isSelected,
                onItemClick = onItemClick
            )
        }
        // Ítem de información fija
        DrawerMenuItem(
            item = MenuDrawerItem("Version 1.0.0", Icons.AutoMirrored.Filled.Article, ""),
            isSelected = false,
            onItemClick = { /* No hay acción */ },
            isInfoItem = true
        )
    }
}

// ----------------------------------------------------------------------------
// HOMESCREEN (CONTENEDOR PRINCIPAL)
// ----------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    openDrawerOnStart: Boolean,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    LaunchedEffect(openDrawerOnStart) {
        if (openDrawerOnStart){
            drawerState.open()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                currentRoute = currentRoute,
                onItemClick = { route ->
                    scope.launch { drawerState.close() }
                    if (route.isNotEmpty()) {
                        navController.navigate(route) {
                            // Configuración de navegación para el drawer
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBarWithMenuAndNotifications(
                    onMenuClick = { scope.launch { drawerState.open() } },
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
                        enabled = openDrawerOnStart,
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
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = Color.Transparent,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),

                        )
                        FilterChip(
                            selected = true,
                            enabled = true,
                            onClick = { },
                            label = { Text("Últimas Rutas") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = Color.Transparent,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            ,
                        )
                        FilterChip(
                            selected = true,
                            enabled = true,
                            onClick = { },
                            label = { Text("Líneas/Mapas") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                containerColor = Color.Transparent,
                                labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            ,
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
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black

                )
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewHomeScreen() {
    MetroLimaGoTheme {
        HomeScreen(
            navController = androidx.navigation.compose.rememberNavController(),
            openDrawerOnStart = false // O no pases este parámetro, porque es false por defecto
        )
    }
}
