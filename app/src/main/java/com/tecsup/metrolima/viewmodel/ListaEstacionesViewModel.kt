package com.tecsup.metrolima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.repository.EstacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.tecsup.metrolima.R

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
                if (repository.getStationCount() == 0) {
                    insertarEstacionesIniciales()
                } else {
                    _estaciones.value = repository.getEstaciones()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun insertarEstacionesIniciales() {
        val estacionesIniciales = listOf(
            Estacion(
                id = 1,
                nombre = "Estación Villa El Salvador",
                distrito = "Villa El Salvador",
                latitud = -12.23456, longitud = -76.98765,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_bayovar
            ),
            Estacion(
                id = 2,
                nombre = "Estación Gamarra",
                distrito = "La Victoria",
                latitud = -12.06789, longitud = -77.02109,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.gamarra //imagen de la estación
            ),
            Estacion(
                id = 3,
                nombre = "Estación Bayóvar",
                distrito = "San Juan de Lurigancho",
                latitud = -11.96789, longitud = -77.01234,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_bayovar
            ),
            // ... Chicos aqui las 23 estaciones restantes
        )

        repository.insertarEstaciones(estacionesIniciales)
        _estaciones.value = repository.getEstaciones()
    }

    companion object {
        fun provideFactory(context: android.content.Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ListaEstacionesViewModel::class.java)) {
                        val dao = MetroLimaDataBase
                            .getDatabase(context)
                            .estacionDao()
                        val repo = EstacionRepository(dao)
                        @Suppress("UNCHECKED_CAST")
                        return ListaEstacionesViewModel(repo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}