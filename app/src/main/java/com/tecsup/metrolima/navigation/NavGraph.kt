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
import com.tecsup.metrolima.presentacion.screens.AcercaAppScreen
import com.tecsup.metrolima.presentacion.screens.ConfigScreen
import com.tecsup.metrolima.presentacion.screens.DetalleEstacionScreen
import com.tecsup.metrolima.presentacion.screens.FavoritosScreen
import com.tecsup.metrolima.presentacion.screens.ListaEstacionScreen
import kotlinx.coroutines.delay
import com.tecsup.metrolima.presentacion.screens.HomeScreen
import com.tecsup.metrolima.presentacion.screens.IniciarRutaScreen
import com.tecsup.metrolima.presentacion.screens.RutaScreen
import com.tecsup.metrolima.presentacion.screens.SplashScreen
//import com.tecsup.metrolima.presentacion.screens.menu.FavoritosScreen
import com.tecsup.metrolima.presentacion.screens.menu.HistorialRutasScreen
import com.tecsup.metrolima.presentacion.screens.menu.LineasMapasScreen
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
    // Crear una única instancia del ViewModel aquí
    val rutaViewModel: RutaViewModel = viewModel(factory = RutaViewModel.provideFactory(context))

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        // RUTAS DEL MENU



        composable("historial_rutas") {
            HistorialRutasScreen(navController = navController)
        }
        composable("lineas_mapas") {
            LineasMapasScreen(navController = navController)
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

        composable("iniciarRuta") {
            IniciarRutaScreen(navController = navController, viewModel = rutaViewModel)
        }

        composable("rutas") {
            RutaScreen(navController = navController, viewModel = rutaViewModel)  // Pasa el mismo ViewModel aquí
        }



        composable("mapa") {
            Text("Mapa interactivo (en desarrollo)", color = Color.Black)
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



        //RUTAS DE DETALLES DE LAS ESTACIONES

        composable(
            route = "detalle/{estacionJson}",
            arguments = listOf(navArgument("estacionJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val estacionJson = backStackEntry.arguments?.getString("estacionJson")
            val estacion = estacionJson?.let { Gson().fromJson(it, Estacion::class.java) }

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