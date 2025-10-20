package com.tecsup.metrolima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.repository.EstacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ListaEstacionesViewModel(
    private val repository: EstacionRepository
) : ViewModel() {

    private val _estaciones = MutableStateFlow<List<Estacion>>(emptyList())
    val estaciones = _estaciones.asStateFlow()

    init {
        cargarEstaciones()
    }

    private fun cargarEstaciones() {
        viewModelScope.launch {
            try {
                val lista = repository.getEstaciones()
                if (lista.isEmpty()) {
                    insertarEstacionesIniciales()
                } else {
                    _estaciones.value = lista
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun insertarEstacionesIniciales() {
        val estacionesIniciales = listOf(
            Estacion(nombre = "Estación Villa El Salvador", direccion = "Villa El Salvador", latitud = -12.234, longitud = -76.934),
            Estacion(nombre = "Estación Atocongo", direccion = "San Juan de Miraflores", latitud = -12.154, longitud = -76.981),
            Estacion(nombre = "Estación Gamarra", direccion = "La Victoria", latitud = -12.065, longitud = -77.015),
            Estacion(nombre = "Estación La Cultura", direccion = "San Borja", latitud = -12.098, longitud = -77.001),
            Estacion(nombre = "Estación Bayóvar", direccion = "San Juan de Lurigancho", latitud = -12.005, longitud = -76.980)
        )

        repository.insertarEstaciones(estacionesIniciales)
        _estaciones.value = repository.getEstaciones()
    }

    companion object {
        fun provideFactory(context: android.content.Context): androidx.lifecycle.ViewModelProvider.Factory {
            return object : androidx.lifecycle.ViewModelProvider.Factory {
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    val dao = com.tecsup.metrolima.data.db.MetroLimaDataBase
                        .getDatabase(context)
                        .estacionDao()
                    val repo = com.tecsup.metrolima.repository.EstacionRepository(dao)
                    @Suppress("UNCHECKED_CAST")
                    return ListaEstacionesViewModel(repo) as T
                }
            }
        }
    }


}
