package com.tecsup.metrolima.ui.components

import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { /* onNavigate("home_route") */ },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* onNavigate("routes_route") */ },
            icon = { Icon(Icons.Filled.LocationOn, contentDescription = "Rutas") },
            label = { Text("Rutas") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* onNavigate("stations_route") */ },
            icon = { Icon(Icons.Filled.Map, contentDescription = "Estaciones") },
            label = { Text("Estaciones") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* onNavigate("settings_route") */ },
            icon = { Icon(Icons.Filled.Settings, contentDescription = "Config.") },
            label = { Text("Config.") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    MetroLimaGoTheme {
        BottomNavigationBar()
    }
}