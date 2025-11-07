package com.tecsup.metrolima.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.metrolima.presentacion.screens.AcercaAppScreen
import com.tecsup.metrolima.presentacion.screens.ConfigScreen
import com.tecsup.metrolima.presentacion.screens.DetalleEstacionScreen
import com.tecsup.metrolima.presentacion.screens.FavoritosScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import com.tecsup.metrolima.presentacion.screens.HomeScreen
import com.tecsup.metrolima.presentacion.screens.IniciarRutaScreen
import com.tecsup.metrolima.presentacion.screens.ListaLineasScreen
import com.tecsup.metrolima.presentacion.screens.MapaScreen
// Importar la nueva pantalla que crearemos
import com.tecsup.metrolima.presentacion.screens.MapaGeneralScreen
import com.tecsup.metrolima.presentacion.screens.RutaScreen
import com.tecsup.metrolima.presentacion.screens.SplashScreen
import com.tecsup.metrolima.presentacion.screens.menu.HistorialRutasScreen
import com.tecsup.metrolima.viewmodel.ListaLineasViewModel
import com.tecsup.metrolima.viewmodel.RutaViewModel


@Composable
fun NavGraph(
    navController: NavHostController,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    selectedLanguage: String,
    onLanguageChange: (String) -> Unit
) {
    val context = LocalContext.current
    // Mantener la instancia del ViewModel para compartirla si es necesario
    val rutaViewModel: RutaViewModel = viewModel(
        factory = RutaViewModel.provideFactory(context)
    )

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // --- RUTAS DEL MENU ---

        composable("historial_rutas") {
            HistorialRutasScreen(navController = navController, viewModel = rutaViewModel)
        }

        composable("acerca_app") {
            AcercaAppScreen(navController = navController)
        }

        // --- RUTAS DEL NAVBAR ---

        composable("splash") {
            SplashScreen(navController = navController)
        }

        // 1. HOME
        composable(
            "home?openDrawer={openDrawer}",
            arguments = listOf(navArgument("openDrawer") {
                type = NavType.BoolType
                defaultValue = false
            })
        ) { backStackEntry ->
            val openDrawer = backStackEntry.arguments?.getBoolean("openDrawer") ?: false
            HomeScreen(navController, openDrawerOnStart = openDrawer)
        }

        //  CALCULAR RUTA
        composable(route = "rutas") {
            RutaScreen(navController = navController, viewModel = rutaViewModel)
        }

        composable(route = "iniciarRuta") {
            IniciarRutaScreen(navController = navController, viewModel = rutaViewModel)
        }

        // MAPA GENERAL (Navbar Central) - Muestra todas las líneas juntas
        composable(route = "mapa_general") {
            MapaGeneralScreen(navController = navController)
        }


        // LÍNEAS DE METRO (Punto de acceso a listados y mapa de línea)
        composable("lineas") {
            val listaLineasViewModel: ListaLineasViewModel = viewModel(
                factory = ListaLineasViewModel.provideFactory(context)
            )
            ListaLineasScreen(navController = navController, viewModel = listaLineasViewModel)
        }

        // Sub-ruta del Listado: Navegación de Línea a Estaciones
        composable(
            route = "estaciones/linea/{lineaId}",
            arguments = listOf(navArgument("lineaId") {
                type = NavType.IntType;
                defaultValue = 0
            })
        ) { backStackEntry ->
            val lineaId = backStackEntry.arguments?.getInt("lineaId")
            ListaEstacionScreen(navController = navController, lineaId = if (lineaId == 0) null else lineaId)
        }

        // Sub-ruta del Listado: Navegación de Línea a su Mapa
        composable(
            route = "mapa_linea/{lineaId}", // **Ruta renombrada**
            arguments = listOf(navArgument("lineaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val lineaId = backStackEntry.arguments?.getInt("lineaId") ?: 1
            MapaScreen(navController = navController, lineaId = lineaId) // MapaScreen usado para 1 línea
        }


        // CONFIGURACIÓN
        composable("config") {
            ConfigScreen(
                navController = navController,
                darkModeEnabled = darkModeEnabled,
                onDarkModeChange = onDarkModeChange,
                selectedLanguage = selectedLanguage,
                onLanguageChange = onLanguageChange
            )
        }

        // --- RUTAS COMPLEMENTARIAS ---

        composable("favoritos") { // Se mantuvo fuera de la secuencia principal del navbar
            FavoritosScreen(navController = navController)
        }


        //RUTAS DE DETALLES DE LAS ESTACIONES
        composable(
            route = "detalle/{estacionId}",
            arguments = listOf(navArgument("estacionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val estacionId = backStackEntry.arguments?.getInt("estacionId") ?: 0
            DetalleEstacionScreen(navController = navController, estacionId = estacionId)
        }
    }
}