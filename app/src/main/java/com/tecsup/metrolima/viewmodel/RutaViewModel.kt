package com.tecsup.metrolima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Ruta
import com.tecsup.metrolima.data.model.RutaResultado
import com.tecsup.metrolima.repository.EstacionRepository
import com.tecsup.metrolima.repository.RutaRepository
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RutaViewModel(
    private val estacionRepository: EstacionRepository,
    private val rutaRepository: RutaRepository
) : ViewModel() {

    private val _allEstaciones = MutableStateFlow<List<Estacion>>(emptyList())
    private val _savedRoutes = MutableStateFlow<List<Ruta>>(emptyList())
    val savedRoutes: StateFlow<List<Ruta>> = _savedRoutes.asStateFlow()

    private val _origenEstacion = MutableStateFlow<Estacion?>(null)
    val origenEstacion: StateFlow<Estacion?> = _origenEstacion.asStateFlow()

    private val _destinoEstacion = MutableStateFlow<Estacion?>(null)
    val destinoEstacion: StateFlow<Estacion?> = _destinoEstacion.asStateFlow()

    private val _searchOrigenText = MutableStateFlow("")
    val searchOrigenText: StateFlow<String> = _searchOrigenText.asStateFlow()

    private val _searchDestinoText = MutableStateFlow("")
    val searchDestinoText: StateFlow<String> = _searchDestinoText.asStateFlow()

    private val _filteredOrigenes = MutableStateFlow<List<Estacion>>(emptyList())
    val filteredOrigenes: StateFlow<List<Estacion>> = _filteredOrigenes.asStateFlow()

    private val _filteredDestinos = MutableStateFlow<List<Estacion>>(emptyList())
    val filteredDestinos: StateFlow<List<Estacion>> = _filteredDestinos.asStateFlow()

    private val _resultadoRuta = MutableStateFlow<RutaResultado?>(null)
    val resultadoRuta: StateFlow<RutaResultado?> = _resultadoRuta.asStateFlow()

    private val _isCurrentRouteFavorite = MutableStateFlow(false)
    val isCurrentRouteFavorite: StateFlow<Boolean> = _isCurrentRouteFavorite.asStateFlow()

    // ------------------ BUSQUEDAS -------------------
    fun onSearchOrigenChange(query: String) {
        _searchOrigenText.value = query
        _filteredOrigenes.value = _allEstaciones.value.filter {
            it.nombre.contains(query, ignoreCase = true)
        }
    }

    fun onSearchDestinoChange(query: String) {
        _searchDestinoText.value = query
        _filteredDestinos.value = _allEstaciones.value.filter {
            it.nombre.contains(query, ignoreCase = true)
        }
    }

    fun onOrigenSelected(estacion: Estacion) {
        _origenEstacion.value = estacion
        _searchOrigenText.value = estacion.nombre
        _filteredOrigenes.value = emptyList()
    }

    fun onDestinoSelected(estacion: Estacion) {
        _destinoEstacion.value = estacion
        _searchDestinoText.value = estacion.nombre
        _filteredDestinos.value = emptyList()
    }

    private fun resolveSelectionsFromTextIfNeeded() {
        if (_origenEstacion.value == null && _searchOrigenText.value.isNotBlank()) {
            _allEstaciones.value.firstOrNull {
                it.nombre.equals(_searchOrigenText.value.trim(), ignoreCase = true)
            }?.let { _origenEstacion.value = it }
        }
        if (_destinoEstacion.value == null && _searchDestinoText.value.isNotBlank()) {
            _allEstaciones.value.firstOrNull {
                it.nombre.equals(_searchDestinoText.value.trim(), ignoreCase = true)
            }?.let { _destinoEstacion.value = it }
        }
    }

    // ------------------ CALCULAR RUTA -------------------
    fun onCalcularRutaClick() {
        resolveSelectionsFromTextIfNeeded()

        val origen = _origenEstacion.value
        val destino = _destinoEstacion.value

        if (origen != null && destino != null) {
            viewModelScope.launch {
                val estacionesIntermedias = simularRuta(origen, destino)
                val tiempoEstimado = "${(estacionesIntermedias.size + 1) * 2} min"

                _resultadoRuta.value = RutaResultado(
                    tiempoEstimado = tiempoEstimado,
                    estacionesIntermedias = estacionesIntermedias
                )
            }
        }
    }

    // --- Opciones de transporte y optimización ---
    private val _selectedTransportOption = MutableStateFlow("Metro")
    val selectedTransportOption: StateFlow<String> = _selectedTransportOption.asStateFlow()

    private val _selectedOptimizationOption = MutableStateFlow("Menos Transbordos")
    val selectedOptimizationOption: StateFlow<String> = _selectedOptimizationOption.asStateFlow()

    fun onTransportOptionSelected(option: String) {
        _selectedTransportOption.value = option
        println("🚇 Transporte seleccionado: $option")
    }

    fun onOptimizationOptionSelected(option: String) {
        _selectedOptimizationOption.value = option
        println("⚙️ Optimización seleccionada: $option")
    }

    // ------------------ FAVORITOS -------------------
    fun saveCurrentRoute() {
        val origen = _origenEstacion.value
        val destino = _destinoEstacion.value
        val resultado = _resultadoRuta.value

        if (origen == null || destino == null || resultado == null) return

        val minutos = resultado.tiempoEstimado.filter { it.isDigit() }.toIntOrNull() ?: 0
        val intermedias = resultado.estacionesIntermedias.joinToString("|") { it.nombre }

        val ruta = Ruta(
            idEstacionOrigen = origen.id,
            nombreEstacionOrigen = origen.nombre,
            idEstacionDestino = destino.id,
            nombreEstacionDestino = destino.nombre,
            tiempoEstimadoMinutos = minutos,
            estacionesIntermedias = intermedias
        )

        viewModelScope.launch {
            rutaRepository.insertRoute(ruta)
            loadSavedRoutes()
            _isCurrentRouteFavorite.value = true
        }
    }

    private fun simularRuta(origen: Estacion, destino: Estacion): List<Estacion> {
        val todas = _allEstaciones.value
        val i1 = todas.indexOfFirst { it.id == origen.id }
        val i2 = todas.indexOfFirst { it.id == destino.id }
        if (i1 == -1 || i2 == -1) return emptyList()
        val inicio = minOf(i1, i2)
        val fin = maxOf(i1, i2)
        return todas.subList(inicio + 1, fin)
    }

    private fun loadSavedRoutes() {
        viewModelScope.launch {
            rutaRepository.allRoutes.collect { routes ->
                _savedRoutes.value = routes
            }
        }
    }

    init {
        viewModelScope.launch {
            estacionRepository.getEstaciones().collect { _allEstaciones.value = it }
        }
        loadSavedRoutes()
    }

    companion object {
        fun provideFactory(context: android.content.Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(RutaViewModel::class.java)) {
                        val db = MetroLimaDataBase.getDatabase(context)
                        val estacionRepo = EstacionRepository(db.estacionDao())
                        val rutaRepo = RutaRepository(db.rutaDao())
                        @Suppress("UNCHECKED_CAST")
                        return RutaViewModel(estacionRepo, rutaRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
