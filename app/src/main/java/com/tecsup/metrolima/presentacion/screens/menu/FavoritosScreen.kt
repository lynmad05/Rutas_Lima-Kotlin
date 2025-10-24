package com.tecsup.metrolima.presentacion.screens.menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.TopAppBarWithMenuAndNotifications

@Composable
fun FavoritosScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBarWithMenuAndNotifications(
                title = stringResource(R.string.favoritos_title),
                onMenuClick = { navController.popBackStack() },
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = stringResource(R.string.pantalla_favoritos))
        }
    }
}
