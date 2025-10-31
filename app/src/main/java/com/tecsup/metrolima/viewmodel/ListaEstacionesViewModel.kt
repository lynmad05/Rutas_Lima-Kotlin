package com.tecsup.metrolima.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tecsup.metrolima.R
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.EstacionExtendida
import com.tecsup.metrolima.data.model.Linea
import com.tecsup.metrolima.repository.EstacionRepository
import com.tecsup.metrolima.repository.LineaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ListaEstacionesViewModel(
    private val estacionRepository: EstacionRepository,
    private val lineaRepository: LineaRepository,
    private val appContext: Context
) : ViewModel() {

    // Flujo de estaciones pero usando ya JOIN
    val estacionesConLinea: StateFlow<List<EstacionExtendida>> = estacionRepository.getAllEstacionesConLinea() // Nuevo método
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /// Texto del campo de búsqueda
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _mensajeUsuario = MutableStateFlow("")
    val mensajeUsuario: StateFlow<String> = _mensajeUsuario.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Lista filtrada ahora filtra Estacion extendida
    val estacionesFiltradas = combine(_searchText, estacionesConLinea) { text, est -> // Usamos estacionesConLinea
        if (text.isBlank()) est
        else est.filter {
            it.nombre.contains(text, ignoreCase = true) ||
                    it.distrito.contains(text, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadLineasLocales()
        cargarDatosIniciales()
    }
    private val _lineas = MutableStateFlow(emptyList<com.tecsup.metrolima.data.model.Linea>())
    private fun loadLineasLocales() {
        viewModelScope.launch {
            try {
                _lineas.value = lineaRepository.getLineasLocales().first()
            } catch (e: Exception) {
                Log.e("ListaEstacionesViewModel", "Error al cargar líneas locales: ${e.message}")
            }
        }
    }


    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
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


    fun cargarDatosIniciales() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                 val currentEstaciones = estacionesConLinea.value
                if (currentEstaciones.isEmpty()) {
                    _mensajeUsuario.value = "Intentando cargar datos iniciales desde la red..."
                    estacionRepository.fetchAndSaveAllData()
                    val estacionesDespuesDeCarga = estacionRepository.getAllEstacionesConLinea().first()
                    if (estacionesDespuesDeCarga.isNotEmpty()) {
                        _mensajeUsuario.value = "Datos cargados correctamente desde la red."
                    } else {
                        _mensajeUsuario.value = "No hay conexión y no se encontraron datos locales."
                    }
                } else {
                    _mensajeUsuario.value = "Mostrando datos locales existentes."
                }
            } catch (e: Exception) {
                Log.e("ListaEstacionesViewModel", "Error general en la carga de datos: ${e.message}", e)
                _mensajeUsuario.value = "Error de conexión o datos. No se pudo actualizar."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun insertarEstacionesIniciales(context: Context) {
        try {
            //  Leer JSON
            val inputStream = context.assets.open("estaciones.json")
            val json = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val tipo = object : TypeToken<List<Estacion>>() {}.type
            val estacionesDesdeJson: List<Estacion> = gson.fromJson(json, tipo)
            val lineasLocales = lineaRepository.getLineasLocales().first()
            val linea1Id = lineasLocales.firstOrNull()?.id ?: 1

            //  Mapear a EstacionExtendida
            val estacionesConImagenes = estacionesDesdeJson.map { estacion ->
                EstacionExtendida(
                    id = 0,
                    nombre = estacion.nombre,
                    distrito = estacion.distrito,
                    lat = 0.0,
                    lon = 0.0,
                    linea_id = linea1Id,
                    horario = estacion.horario,
                    imagenCircular = estacion.imagenCircular,
                    imagenCircularResId = when (estacion.nombre) {
                        "Estación Bayóvar" -> R.drawable.estacion_bayovar
                        "Estación Santa Rosa" -> R.drawable.estacion_santarosa
                        "San Martín" -> R.drawable.estacion_sanmartin
                        "San Carlos" -> R.drawable.estacion_sancarlos
                        "Los Postes" -> R.drawable.estacion_lospostes
                        "Los Jardines" -> R.drawable.estacion_losjardines
                        "Pirámide del Sol" -> R.drawable.estacion_piramidedelsol
                        "Caja de Agua" -> R.drawable.estacion_cajadeagua
                        "Presbítero Maestro" -> R.drawable.estacion_presbiteromaestro
                        "El Ángel" -> R.drawable.estacion_elangel
                        "Miguel Grau" -> R.drawable.estacion_miguelgrau
                        "Gamarra" -> R.drawable.estacion_gamarra
                        "Arriola" -> R.drawable.estacion_arriola
                        "La Cultura" -> R.drawable.estacion_cultura
                        "San Borja Sur" -> R.drawable.estacion_sanborjasur
                        "Estación Angamos" -> R.drawable.estacion_angamos
                        "Cabitos" -> R.drawable.estacion_cabitos
                        "Ayacucho" -> R.drawable.estacion_ayacucho
                        "Jorge Chávez" -> R.drawable.estacion_jorgechavez
                        "Atocongo" -> R.drawable.estacion_atocongo
                        "San Juan" -> R.drawable.estacion_sanjuan
                        "María Auxilidora" -> R.drawable.estacion_mariaauxiliadora
                        "Villa María" -> R.drawable.estacion_villamaria
                        "Pumacahua" -> R.drawable.estacion_pumacahua
                        "Parque Industrial" -> R.drawable.estacion_parque_industrial
                        "Villa El Salvador" -> R.drawable.estacion_villa_salvador
                        else -> R.drawable.ic_launcher_foreground
                    }
                )
            }

            // Insertar en la BD
            estacionRepository.insertarEstacionesExtendidas(estacionesConImagenes)
            println("Estaciones insertadas desde JSON en BD local")
        } catch (e: Exception) {
            println("Error al insertar estaciones iniciales: ${e.message}")
        }
    }



    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ListaEstacionesViewModel::class.java)) {
                        val db = MetroLimaDataBase.getDatabase(context)
                        val lineaDao = db.lineaDao()
                        val lineaRepo = LineaRepository(lineaDao)
                        val estacionDao = db.estacionDao()
                        val estacionRepo = EstacionRepository(estacionDao, lineaRepo)

                        @Suppress("UNCHECKED_CAST")
                        return ListaEstacionesViewModel(estacionRepo, lineaRepo, context) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
