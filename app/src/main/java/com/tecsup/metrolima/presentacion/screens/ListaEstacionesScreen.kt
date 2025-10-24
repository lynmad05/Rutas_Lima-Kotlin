package com.tecsup.metrolima.presentacion.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.tecsup.metrolima.R
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.ui.components.BottomNavigationBar
import com.tecsup.metrolima.ui.components.TopAppBarEstaciones
import com.tecsup.metrolima.ui.theme.*
import com.tecsup.metrolima.viewmodel.ListaEstacionesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaEstacionScreen(
    navController: NavHostController,
    onMenuClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val viewModel: ListaEstacionesViewModel = viewModel(
        factory = ListaEstacionesViewModel.provideFactory(context)
    )

    val estaciones by viewModel.estacionesFiltradas.collectAsState()
    val mensajeUsuario by viewModel.mensajeUsuario.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchText by viewModel.searchText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(mensajeUsuario) {
        if (mensajeUsuario.isNotBlank()) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = mensajeUsuario,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBarEstaciones(
                title = stringResource(R.string.titulo_estaciones)
            )
        },
        bottomBar = { BottomNavigationBar(navController = navController) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            //  Barra de búsqueda traducible
            OutlinedTextField(
                value = searchText,
                onValueChange = { viewModel.onSearchTextChange(it) },
                placeholder = { Text(stringResource(R.string.buscar_placeholder)) },
                leadingIcon = {
                    Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.buscar_placeholder))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { keyboardController?.hide() }
                ),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SearchBarBackground,
                    unfocusedContainerColor = SearchBarBackground,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = SearchBarContentColor,
                    unfocusedTextColor = SearchBarContentColor,
                    focusedLeadingIconColor = SearchBarContentColor,
                    unfocusedLeadingIconColor = SearchBarContentColor,
                    focusedPlaceholderColor = SearchBarContentColor,
                    unfocusedPlaceholderColor = SearchBarContentColor
                )
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    }
                }

                estaciones.isEmpty() && mensajeUsuario.startsWith("❌").not() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_estaciones),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(estaciones) { estacion ->
                            StationListItem(
                                estacion = estacion,
                                onClick = {
                                    val estacionJson = Uri.encode(Gson().toJson(estacion))
                                    navController.navigate("detalle/${estacionJson}")
                                }
                            )
                            Divider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = DividerColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StationListItem(estacion: Estacion, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            painter = painterResource(id = estacion.imagenCircularResId),
            contentDescription = estacion.nombre,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(90.dp)
                .height(70.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = estacion.nombre,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = estacion.distrito,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Filled.ArrowForwardIos,
            contentDescription = stringResource(R.string.ir_detalle, estacion.nombre),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun PreviewListaEstacionScreen() {
    MetroLimaGoTheme {
        val navController = rememberNavController()
        Surface(color = MaterialTheme.colorScheme.background) {
            ListaEstacionScreen(navController = navController)
        }
    }
}
