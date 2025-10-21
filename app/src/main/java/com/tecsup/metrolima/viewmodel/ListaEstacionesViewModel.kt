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
                nombre = "Estación Bayóvar",
                distrito = "San Juan de Lurigancho",
                latitud = -11.96789, longitud = -77.01234,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_bayovar
            ),
            Estacion(
                id = 2,
                nombre = "Estación Santa Rosa",
                distrito = "San Juan de Lurigancho",
                latitud = -12.00429, longitud = -76.96373,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_santarosa
            ),
            Estacion(
                id = 3,
                nombre = "San Martín",
                distrito = "San Juan de Lurigancho",
                latitud = -12.00958, longitud = -76.97084,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_sanmartin
            ),
            Estacion(
                id = 4,
                nombre = "San Carlos",
                distrito = "San Juan de Lurigancho",
                latitud = -12.01312, longitud = -76.97626,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_sancarlos
            ),
            Estacion(
                id = 5,
                nombre = "Los Postes",
                distrito = "San Juan de Lurigancho",
                latitud = -12.01811, longitud = -76.98313,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_lospostes
            ),
            Estacion(
                id = 6,
                nombre = "Los Jardines",
                distrito = "San Juan de Lurigancho",
                latitud = -12.02241, longitud = -76.98928,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_losjardines
            ),
            Estacion(
                id = 7,
                nombre = "Pirámide del Sol",
                distrito = "San Juan de Lurigancho",
                latitud = -12.02916, longitud = -76.99842,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_piramidedelsol
            ),
            Estacion(
                id = 8,
                nombre = "Caja de Agua",
                distrito = "San Juan de Lurigancho",
                latitud = -12.03718, longitud = -77.00791,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_cajadeagua
            ),
            Estacion(
                id = 9,
                nombre = "Presbítero Maestro",
                distrito = "El Agustino",
                latitud = -12.05756, longitud = -77.03523,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_presbiteromaestro
            ),
            Estacion(
                id = 10,
                nombre = "El Ángel",
                distrito = "El Agustino",
                latitud = -12.06108, longitud = -77.02948,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_elangel
            ),
            Estacion(
                id = 11,
                nombre = "Miguel Grau",
                distrito = "Cercado de Lima",
                latitud = -12.06748, longitud = -77.02334,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_miguelgrau
            ),
            Estacion(
                id = 12,
                nombre = "Gamarra",
                distrito = "La Victoria",
                latitud = -12.07611, longitud = -77.01617,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_gamarra
            ),
            Estacion(
                id = 13,
                nombre = "Arriola",
                distrito = "La Victoria",
                latitud = -12.08661, longitud = -77.01309,
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