package com.tecsup.metrolima.presentacion.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tecsup.metrolima.R
import com.tecsup.metrolima.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mapa de la Línea 1",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp,
                            color = Color(0xFF004D73)
                        )
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFE1F5FE)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE3F2FD), Color(0xFFFFFFFF))
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 🗺 Imagen principal más grande y con bordes suaves
            Image(
                painter = painterResource(id = R.drawable.mapa_linea1),
                contentDescription = "Mapa de la Línea 1 del Metro de Lima",
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(420.dp)
                    .shadow(10.dp, RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "La Línea 1 del Metro de Lima une los distritos del sur con el centro y el este de la ciudad. " +
                        "Con 26 estaciones, recorre desde Villa El Salvador hasta San Juan de Lurigancho, " +
                        "pasando por zonas clave como Villa María, San Borja y La Victoria.\n\n" +
                        "Contribuye al desarrollo urbano ofreciendo una alternativa de transporte rápida, " +
                        "segura y ecológica para miles de limeños cada día.",
                color = Color(0xFF1B1B1B),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 25.sp,
                    textAlign = TextAlign.Justify
                ),
                modifier = Modifier
                    .padding(horizontal = 26.dp)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
