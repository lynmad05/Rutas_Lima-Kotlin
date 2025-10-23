package com.tecsup.metrolima.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first

class ListaEstacionesViewModel(
    private val repository: EstacionRepository
) : ViewModel() {

    // Cambiado para ser un Flow que se recolecta del repositorio
    private val _estaciones = MutableStateFlow<List<Estacion>>(emptyList())
    // Hacemos que estaciones sea un StateFlow que colecta del repositorio,
    // así siempre refleja el estado de la base de datos de Room
    val estaciones: StateFlow<List<Estacion>> = repository.getEstaciones()
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // Empieza a colectar cuando hay suscriptores
            initialValue = emptyList()
        )

    // 🔍 Texto del campo de búsqueda
    private val _searchText = MutableStateFlow( "")
    val searchText = _searchText.asStateFlow()

    private val _mensajeUsuario = MutableStateFlow("")
    val mensajeUsuario: StateFlow<String> = _mensajeUsuario.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // 🔹 Lista filtrada (resultado de búsqueda)
    // Ahora combine(_searchText, estaciones) usa el StateFlow 'estaciones' que ya recolecta del repo
    val estacionesFiltradas = combine(_searchText, estaciones) { text, est -> // est es List<Estacion>
        if (text.isBlank()) {
            est
        } else {
            est.filter {
                it.nombre.contains(text, ignoreCase = true) ||
                        it.distrito.contains(text, ignoreCase = true) ||
                        it.linea.contains(text, ignoreCase = true)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    private val ESTACIONES_TOTAL_ESPERADAS = 26

    init {
        // Inicialmente cargamos desde API remota o respaldo local
        cargarEstacionesRemotas()
    }

    private fun asignarImagenLocal(nombre: String?): Int {
        return when (nombre) {
            "estacion_bayovar" -> R.drawable.estacion_bayovar
            "estacion_santarosa" -> R.drawable.estacion_santarosa
            "estacion_sanmartin" -> R.drawable.estacion_sanmartin
            "estacion_sancarlos" -> R.drawable.estacion_sancarlos
            "estacion_lospostes" -> R.drawable.estacion_lospostes
            "estacion_losjardines" -> R.drawable.estacion_losjardines
            "estacion_piramidedelsol" -> R.drawable.estacion_piramidedelsol
            "estacion_cajadeagua" -> R.drawable.estacion_cajadeagua
            "estacion_presbiteromaestro" -> R.drawable.estacion_presbiteromaestro
            "estacion_elangel" -> R.drawable.estacion_elangel
            "estacion_miguelgrau" -> R.drawable.estacion_miguelgrau
            "estacion_gamarra" -> R.drawable.estacion_gamarra
            "estacion_arriola" -> R.drawable.estacion_arriola
            "estacion_cultura" -> R.drawable.estacion_cultura
            "estacion_sanborjasur" -> R.drawable.estacion_sanborjasur
            "estacion_angamos" -> R.drawable.estacion_angamos
            "estacion_cabitos" -> R.drawable.estacion_cabitos
            "estacion_ayacucho" -> R.drawable.estacion_ayacucho
            "estacion_jorgechavez" -> R.drawable.estacion_jorgechavez
            "estacion_atocongo" -> R.drawable.estacion_atocongo
            "estacion_sanjuan" -> R.drawable.estacion_sanjuan
            "estacion_mariaauxiliadora" -> R.drawable.estacion_mariaauxiliadora
            "estacion_villamaria" -> R.drawable.estacion_villamaria
            "estacion_pumacahua" -> R.drawable.estacion_pumacahua
            "estacion_parque_industrial" -> R.drawable.estacion_parque_industrial
            "estacion_villa_salvador" -> R.drawable.estacion_villa_salvador
            else -> R.drawable.ic_launcher_foreground
        }
    }

    // Esta función cargarEstaciones ahora SÍ maneja los Flows correctamente
    // y solo inserta si es necesario, y luego depende del StateFlow 'estaciones'
    private fun cargarEstaciones() {
        viewModelScope.launch {
            try {
                // Obtener el conteo como un Int, no como un Flow
                val count = repository.getStationCount().first() // <-- CORRECCIÓN AQUÍ

                if (count < ESTACIONES_TOTAL_ESPERADAS) {
                    insertarEstacionesIniciales()
                }
                // NO _estaciones.value = repository.getEstaciones() aquí
                // porque 'estaciones' (el StateFlow público) ya está colectando del repositorio
                // y se actualizará automáticamente cuando insertarEstacionesIniciales()
                // cambie la base de datos.

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun refrescarEstaciones() {

        _mensajeUsuario.value = "Estaciones refrescadas."
    }

    fun cargarEstacionesRemotas() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val estacionesRemotas = repository.getEstacionesRemotas().map { estacion ->
                    estacion.copy(imagenCircularResId = asignarImagenLocal(estacion.imagenCircular))
                }
                if (estacionesRemotas.isNotEmpty()) {
                    repository.deleteAllEstaciones()
                    repository.insertarEstaciones(estacionesRemotas)
                    _mensajeUsuario.value = "Datos actualizados correctamente."
                    Log.d("ListaEstacionesViewModel", "Estaciones cargadas desde API remoto (${estacionesRemotas.size})")
                } else {
                    Log.w("ListaEstacionesViewModel", "API remota devolvió lista vacía. Usando respaldo local.")
                    val currentEstaciones = estaciones.first()
                    if (currentEstaciones.isEmpty()) {
                        insertarEstacionesIniciales()
                        _mensajeUsuario.value = "API vacía. Datos iniciales cargados."
                    } else {
                        _mensajeUsuario.value = "API vacía. Mostrando datos locales existentes."
                    }
                }
            } catch (e: Exception) {
                Log.e("ListaEstacionesViewModel", "Error al cargar estaciones remotas: ${e.message}", e)
                val currentEstaciones = repository.getEstaciones().first()
                if (currentEstaciones.isEmpty()) {
                    insertarEstacionesIniciales()
                    _mensajeUsuario.value = "No hay conexión y no se encontraron datos locales. Mostrando datos iniciales."
                } else {
                    _mensajeUsuario.value = "No se pudo conectar. Mostrando datos locales."
                }
            } finally {
                _isLoading.value = false
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
                imagenCircularResId = R.drawable.estacion_arriola
            ),
            Estacion(
                id = 14,
                nombre = "La Cultura",
                distrito = "San Borja",
                latitud = -12.09228, longitud = -77.00433,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_cultura
            ),
            Estacion(
                id = 15,
                nombre = "San Borja Sur",
                distrito = "San Borja",
                latitud = -12.09818, longitud = -76.99427,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_sanborjasur
            ),
            Estacion(
                id = 16,
                nombre = "Estación Angamos",
                distrito = "Surquillo",
                latitud = -12.1039, longitud = -76.9869,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_angamos
            ),
            Estacion(
                id = 17,
                nombre = "Cabitos",
                distrito = "Santiago de Surco",
                latitud = -12.10731, longitud = -76.97541,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_cabitos
            ),
            Estacion(
                id = 18,
                nombre = "Ayacucho",
                distrito = " San Juan de Miraflores",
                latitud = -12.11363, longitud = -76.97691,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_ayacucho
            ),
            Estacion(
                id = 19,
                nombre = "Jorge Chávez",
                distrito = "Santiago de Surco",
                latitud = -12.12083, longitud = -76.97732,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_jorgechavez
            ),
            Estacion(
                id = 20,
                nombre = "Atocongo",
                distrito = "San Juan de Miraflores",
                latitud = -12.13255, longitud = -76.97273,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_atocongo
            ),
            Estacion(
                id = 21,
                nombre = "San Juan",
                distrito = "San Juan de Miraflores",
                latitud = -12.14379, longitud = -76.96791,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_sanjuan
            ),
            Estacion(
                id = 22,
                nombre = "María Auxilidora",
                distrito = "San Juan de Miraflores",
                latitud = -12.15554, longitud = -76.95809,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_mariaauxiliadora
            ),
            Estacion(
                id = 23,
                nombre = "Villa María",
                distrito = "Villa María del Triunfo",
                latitud = -12.16508, longitud = -76.95028,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_villamaria
            ),
            Estacion(
                id = 24,
                nombre = "Pumacahua",
                distrito = "Villa María del Triunfo",
                latitud = -12.17669, longitud = -76.94482,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_pumacahua
            ),
            Estacion(
                id = 25,
                nombre = "Parque Industrial",
                distrito = "Villa El Salvador",
                latitud = -12.18845, longitud = -76.94023,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_parque_industrial
            ),
            Estacion(
                id = 26,
                nombre = "Villa El Salvador",
                distrito = "Villa El Salvador",
                latitud = -12.19823, longitud = -76.93588,
                linea = "Línea 1 Metro",
                horario = "L-S: 06:00 - 22:00, D/F: 06:00 - 21:00",
                imagenCircularResId = R.drawable.estacion_villa_salvador
            )
        )

        repository.insertarEstaciones(estacionesIniciales)
        // Ya no necesitas _estaciones.value = repository.getEstaciones() aquí
        // porque el StateFlow 'estaciones' (val estaciones = repository.getEstaciones().stateIn(...))
        // ya está observando los cambios en la base de datos de Room y se actualizará automáticamente.
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