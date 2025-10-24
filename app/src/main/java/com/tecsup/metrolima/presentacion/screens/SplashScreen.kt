package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.tecsup.metrolima.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    // ⏳ Retraso de 3 segundos antes de ir al Home
    LaunchedEffect(Unit) {
        delay(3000L)
        navController.popBackStack()
        navController.navigate("home") {
            popUpTo("home") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Imagen de fondo del splash
        Image(
            painter = painterResource(id = R.drawable.welcome),
            contentDescription = stringResource(R.string.splash_image_desc),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // Indicador de carga
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            CircularProgressIndicator(
                modifier = Modifier.wrapContentSize(Alignment.Center),
                color = Color(0xFFC084FC),
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
