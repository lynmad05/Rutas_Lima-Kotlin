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
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarWithMenuAndNotifications
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.ui.theme.SearchBarBackground
import kotlinx.coroutines.launch

data class MenuDrawerItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@Composable
fun menuItems() = listOf(
    MenuDrawerItem(stringResource(R.string.drawer_favoritos), Icons.Default.FavoriteBorder, "favoritos"),
    MenuDrawerItem(stringResource(R.string.drawer_historial), Icons.Default.Restore, "historial_rutas"),
    MenuDrawerItem(stringResource(R.string.drawer_acerca), Icons.Default.Info, "acerca_app"),
)

@Composable
fun DrawerMenuItem(
    item: MenuDrawerItem,
    isSelected: Boolean,
    onItemClick: (String) -> Unit,
    isInfoItem: Boolean = false
) {
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val contentColor =
        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val infoBackgroundColor =
        if (isInfoItem) MaterialTheme.colorScheme.surfaceVariant else backgroundColor

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
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = stringResource(R.string.menu_title),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(20.dp)
        )
        Spacer(Modifier.height(8.dp))

        menuItems().forEach { item ->
            val isSelected = currentRoute == item.route
            DrawerMenuItem(
                item = item,
                isSelected = isSelected,
                onItemClick = onItemClick
            )
        }

        DrawerMenuItem(
            item = MenuDrawerItem(stringResource(R.string.version_app), Icons.AutoMirrored.Filled.Article, ""),
            isSelected = false,
            onItemClick = { },
            isInfoItem = true
        )
    }
}

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
        if (openDrawerOnStart) {
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
                        placeholder = { Text(stringResource(R.string.buscar_placeholder), color = Color.Black) },
                        leadingIcon = {
                            Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.buscar_placeholder), tint = Color.Black)
                        },
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
                            unfocusedBorderColor = Color.Transparent
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
                            onClick = { },
                            label = { Text(stringResource(R.string.filtro_favoritos)) }
                        )
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = { Text(stringResource(R.string.filtro_ultimas)) }
                        )
                        FilterChip(
                            selected = true,
                            onClick = { },
                            label = { Text(stringResource(R.string.filtro_lineas)) }
                        )
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.lugares_populares),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            PopularPlaceCard(
                                title = stringResource(R.string.ruta),
                                subtitulo = "Estación Central a Miraflores",
                                description = "Línea 1",
                                imageUrl = R.drawable.estacion_central
                            )
                        }
                        item {
                            PopularPlaceCard(
                                title = stringResource(R.string.estacion),
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
                            text = stringResource(R.string.proximamente),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.Construction,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Text(
                        text = stringResource(R.string.mas_lineas),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.lineas_en_desarrollo),
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
                            contentDescription = stringResource(R.string.tren_construccion),
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    style = MaterialTheme.typography.bodySmall
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
            openDrawerOnStart = false
        )
    }
}
