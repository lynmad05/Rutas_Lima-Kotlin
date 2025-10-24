package com.tecsup.metrolima.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
import com.tecsup.metrolima.ui.theme.NavigationBarBackground
import com.tecsup.metrolima.ui.theme.SelectedButtonColor
import com.tecsup.metrolima.ui.theme.OnSelectedButtonColor
import com.tecsup.metrolima.ui.theme.UnselectedIconColor

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // 🔤 Textos traducibles
    val navItems = listOf(
        BottomNavItem(
            icon = Icons.Filled.Home,
            description = stringResource(R.string.nav_inicio),
            route = "home"
        ),
        BottomNavItem(
            icon = Icons.Filled.LocationOn,
            description = stringResource(R.string.nav_rutas),
            route = "rutas"
        ),
        BottomNavItem(
            icon = Icons.Filled.Map,
            description = stringResource(R.string.nav_mapa),
            route = "mapa"
        ),
        BottomNavItem(
            icon = Icons.Filled.DirectionsTransit,
            description = stringResource(R.string.nav_estaciones),
            route = "listado"
        ),
        BottomNavItem(
            icon = Icons.Filled.Settings,
            description = stringResource(R.string.nav_configuracion),
            route = "config"
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(
                    width = 0.5.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(32.dp)
                ),
            color = NavigationBarBackground,
            shadowElevation = 8.dp
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

                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                if (isSelected) SelectedButtonColor
                                else Color.Transparent
                            )
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
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
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
