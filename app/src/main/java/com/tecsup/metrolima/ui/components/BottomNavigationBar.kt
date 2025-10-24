package com.tecsup.metrolima.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tecsup.metrolima.presentacion.LocalAppContext
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.ui.theme.NavigationBarBackground
import com.tecsup.metrolima.ui.theme.SelectedButtonColor
import com.tecsup.metrolima.ui.theme.OnSelectedButtonColor
import com.tecsup.metrolima.ui.theme.UnselectedIconColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navItems = listOf(
        BottomNavItem(Icons.Filled.Home, "Inicio", "home"),
        BottomNavItem(Icons.Filled.LocationOn, "Rutas", "rutas"),
        BottomNavItem(Icons.Filled.Map, "Mapa", "mapa"),
        BottomNavItem(Icons.Filled.DirectionsTransit, "Estaciones", "listado"),
        BottomNavItem(Icons.Filled.Settings, "Configuración", "config")
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .clip(RoundedCornerShape(40.dp))
                .shadow(elevation = 10.dp, shape = RoundedCornerShape(40.dp)),
            color = NavigationBarBackground,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                navItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    val backgroundColor by animateColorAsState(
                        targetValue = if (isSelected) SelectedButtonColor else Color.Transparent,
                        label = ""
                    )
                    val iconScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.2f else 1f,
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(backgroundColor)
                            .clickable {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.description,
                            tint = if (isSelected) OnSelectedButtonColor else UnselectedIconColor,
                            modifier = Modifier
                                .size(34.dp)
                                .graphicsLayer(scaleX = iconScale, scaleY = iconScale)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LocalizedText(resId: Int) {
    val context = LocalAppContext.current
    Text(text = context.getString(resId))
}

data class BottomNavItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val description: String,
    val route: String
)

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    MetroLimaGoTheme {
        val navController = androidx.navigation.compose.rememberNavController()
        Surface(color = MaterialTheme.colorScheme.background) {
            BottomNavigationBar(navController = navController)
        }
    }
}
