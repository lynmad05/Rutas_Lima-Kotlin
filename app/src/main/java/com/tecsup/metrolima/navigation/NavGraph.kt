package com.tecsup.metrolima.navigation

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.EstacionExtendida
import com.tecsup.metrolima.presentacion.screens.AcercaAppScreen
import com.tecsup.metrolima.presentacion.screens.ConfigScreen
import com.tecsup.metrolima.presentacion.screens.DetalleEstacionScreen
import com.tecsup.metrolima.presentacion.screens.EstacionesPorLineaScreen
import com.tecsup.metrolima.presentacion.screens.FavoritosScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import kotlinx.coroutines.delay
import com.tecsup.metrolima.presentacion.screens.HomeScreen
import com.tecsup.metrolima.presentacion.screens.IniciarRutaScreen
import com.tecsup.metrolima.presentacion.screens.ListaLineasScreen
import com.tecsup.metrolima.presentacion.screens.MapaScreen
import com.tecsup.metrolima.presentacion.screens.RutaScreen
import com.tecsup.metrolima.presentacion.screens.SplashScreen
//import com.tecsup.metrolima.presentacion.screens.menu.FavoritosScreen
import com.tecsup.metrolima.presentacion.screens.menu.HistorialRutasScreen
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
    val sharedViewModel: RutaViewModel = viewModel(
        factory = RutaViewModel.provideFactory(context)
    )
    // Crear una única instancia del ViewModel aquí
    val rutaViewModel: RutaViewModel = viewModel(factory = RutaViewModel.provideFactory(context))

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // RUTAS DEL MENU



        composable("historial_rutas") {
            HistorialRutasScreen(navController = navController, viewModel = rutaViewModel)
        }

        composable("acerca_app") {
            AcercaAppScreen(navController = navController)
        }

        // RUTAS DEL NAVBAR

        composable("splash") {
            SplashScreen(navController = navController)
        }
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

        composable("favoritos") {
            FavoritosScreen(navController = navController)
        }

        composable(route = "rutas") {
            RutaScreen(navController = navController, viewModel = sharedViewModel)
        }

        composable(route = "iniciarRuta") {
            IniciarRutaScreen(navController = navController, viewModel = sharedViewModel)
        }



        composable(route = "mapa") {
            MapaScreen(navController = navController)
        }

        composable("listado") {
            ListaEstacionScreen(navController = navController)
        }

        composable("config") {
            ConfigScreen(
                navController = navController,
                darkModeEnabled = darkModeEnabled,
                onDarkModeChange = onDarkModeChange,
                selectedLanguage = selectedLanguage,
                onLanguageChange = onLanguageChange
            )
        }

        composable("listado_lineas") {
            ListaLineasScreen(navController)
        }

        composable("estaciones_linea/{lineaId}") { backStackEntry ->
            val lineaId = backStackEntry.arguments?.getString("lineaId")?.toInt() ?: 0
            ListaEstacionesPorLineaScreen(navController, lineaId)
        }

        composable(
            route = "estaciones_linea/{lineaId}",
            arguments = listOf(navArgument("lineaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val lineaId = backStackEntry.arguments?.getInt("lineaId") ?: 0
            EstacionesPorLineaScreen(navController = navController, lineaId = lineaId)
        }





        //RUTAS DE DETALLES DE LAS ESTACIONES

        composable(
            route = "detalle/{estacionJson}",
            arguments = listOf(navArgument("estacionJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val estacionJson = backStackEntry.arguments?.getString("estacionJson")
            val estacion = estacionJson?.let { Gson().fromJson(it, EstacionExtendida::class.java) }

            if (estacion != null) {
                DetalleEstacionScreen(
                    navController = navController,
                    estacion = estacion
                )
            } else {
                Text(
                    "Error: Estación no encontrada. Volviendo...",
                    color = Color.Red
                )
                LaunchedEffect(Unit) {
                    delay(1500)
                    navController.popBackStack()
                }
            }
        }



    }
}

@Composable
fun ListaEstacionesPorLineaScreen(x0: NavHostController, x1: Int) {
    TODO("Not yet implemented")
}