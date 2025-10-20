package com.tecsup.metrolima.ui.components

import android.view.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tecsup.metrolima.ui.theme.MetroLimaGoTheme
// ¡Importa tus nuevos colores!
import com.tecsup.metrolima.ui.theme.NavigationBarBackground
import com.tecsup.metrolima.ui.theme.SelectedButtonColor
import com.tecsup.metrolima.ui.theme.OnSelectedButtonColor
import com.tecsup.metrolima.ui.theme.UnselectedIconColor
import androidx.navigation.NavController


@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
    selectedItemIndex: Int = 0
){
    var selectedIndex by remember { mutableStateOf(selectedItemIndex) }

    val items = listOf(
        Icons.Filled.Home,
        Icons.Filled.LocationOn,
        Icons.Filled.Map,
        Icons.Filled.DirectionsTransit,
        Icons.Filled.Settings
    )

    val descriptions = listOf(
        "Inicio",
        "Rutas",
        "Estaciones",
        "Tren",
        "Configuración"
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
                items.forEachIndexed { index, icon ->
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(60.dp)
                            .clip(RoundedCornerShape(30.dp))
                            .background(
                                if (selectedIndex == index) SelectedButtonColor
                                else Color.Transparent
                            )
                            .clickable {
                                selectedIndex = index
                                when (index) {
                                    0 -> navController.navigate("home")  // 🏠 Ir a Home
                                    1 -> navController.navigate("rutas") // 📍 Rutas
                                    2 -> navController.navigate("listado") // 🚉 Estaciones
                                    3 -> navController.navigate("tren")  // 🚆 (si agregas más adelante)
                                    4 -> navController.navigate("config") // ⚙️ Configuración
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = descriptions[index],
                            tint = if (selectedIndex == index) OnSelectedButtonColor
                            else UnselectedIconColor,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigationBar() {
    MetroLimaGoTheme {
        val navController = androidx.navigation.compose.rememberNavController()
        Surface(color = MaterialTheme.colorScheme.background) {
            BottomNavigationBar(
                navController = navController,
                selectedItemIndex = 0
            )
        }
    }
}