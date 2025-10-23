package com.tecsup.metrolima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.room.Query
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Ruta
import com.tecsup.metrolima.data.model.RutaResultado
import com.tecsup.metrolima.repository.EstacionRepository
import com.tecsup.metrolima.repository.RutaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Recibiendo los dos repositorios: Estacion y rutas
class RutaViewModel(
    private val estacionRepository: EstacionRepository,
    private val rutaRepository: RutaRepository
) : ViewModel() {

    private val _allEstaciones = MutableStateFlow<List<Estacion>>(emptyList())
    val allEstaciones: StateFlow<List<Estacion>> = _allEstaciones.asStateFlow()

    private val _origenEstacion = MutableStateFlow<Estacion?>(null)
    val origenEstacion: StateFlow<Estacion?> = _origenEstacion.asStateFlow()

    private val _destinoEstacion = MutableStateFlow<Estacion?>(null)
    val destinoEstacion: StateFlow<Estacion?> = _destinoEstacion.asStateFlow()

    private val _tiempoEstimadoMinutos = MutableStateFlow<Int?>(null)
    val tiempoEstimadoMinutos: StateFlow<Int?> = _tiempoEstimadoMinutos.asStateFlow()


    private val _rutaPasos = MutableStateFlow<List<String>>(emptyList())
    val rutaPasos: StateFlow<List<String>> = _rutaPasos.asStateFlow()

    private val _savedRoutes = MutableStateFlow<List<Ruta>>(emptyList())
    val savedRoutes: StateFlow<List<Ruta>> = _savedRoutes.asStateFlow()

    // Estados para UI
    private val _showOriginPicker = MutableStateFlow(false)
    val showOriginPicker: StateFlow<Boolean> = _showOriginPicker.asStateFlow()

    private val _showDestinoPicker = MutableStateFlow(false)
    val showDestinoPicker: StateFlow<Boolean> = _showDestinoPicker.asStateFlow()

    private val _isCurrentRouteFavorite = MutableStateFlow(false)
    val isCurrentRouteFavorite: StateFlow<Boolean> = _isCurrentRouteFavorite.asStateFlow()


    //Texto que escribirá el usuario
    private val _searchOrigenText = MutableStateFlow("")
    val searchOrigenText: StateFlow<String> = _searchOrigenText.asStateFlow()

    private val _searchDestinoText = MutableStateFlow("")
    val searchDestinoText: StateFlow<String> = _searchDestinoText.asStateFlow()

    // Estaciones filtradas según busqueda

    private val _filteredOrigenes = MutableStateFlow<List<Estacion>>(emptyList())
    val filteredOrigenes: StateFlow<List<Estacion>> = _filteredOrigenes.asStateFlow()

    private val _filteredDestinos = MutableStateFlow<List<Estacion>>(emptyList())
    val filteredDestinos: StateFlow<List<Estacion>> = _filteredDestinos.asStateFlow()

    private val _selectedTransportOption = MutableStateFlow("Metro")
    val selectedTransportOption: StateFlow<String> = _selectedTransportOption.asStateFlow()

    private val _selectedOptimizationOption = MutableStateFlow("Menos Transbordos")
    val selectedOptimizationOption: StateFlow<String> = _selectedOptimizationOption.asStateFlow()

    // --- Resultado de cálculo de ruta (Livia) ---
    private val _resultadoRuta = MutableStateFlow<RutaResultado?>(null)
    val resultadoRuta: StateFlow<RutaResultado?> = _resultadoRuta.asStateFlow()

    fun onTransportOptionSelected(option: String){
        _selectedTransportOption.value = option
        println("🚇 Transporte seleccionado: $option")
    }

    fun onOptimizationOptionSelected(option: String){
        _selectedOptimizationOption.value = option
        println("⚙️ Optimización seleccionada: $option")
    }

    fun onSearchOrigenChange(query: String){
        _searchOrigenText.value = query
        _filteredOrigenes.value = _allEstaciones.value.filter {
            it.nombre.contains(query, ignoreCase = true)
        }
    }

    fun onSearchDestinoChange(query: String){
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
        println("Resolviendo estaciones desde texto...")
        if (_origenEstacion.value == null && _searchOrigenText.value.isNotBlank()) {
            val encontrado = _allEstaciones.value.firstOrNull {
                it.nombre.trim().equals(_searchOrigenText.value.trim(), ignoreCase = true)
            }
            println("Origen encontrado: $encontrado")
            if (encontrado != null) _origenEstacion.value = encontrado
        }
        if (_destinoEstacion.value == null && _searchDestinoText.value.isNotBlank()) {
            val encontrado = _allEstaciones.value.firstOrNull {
                it.nombre.trim().equals(_searchDestinoText.value.trim(), ignoreCase = true)
            }
            println("Destino encontrado: $encontrado")
            if (encontrado != null) _destinoEstacion.value = encontrado
        }
    }



    fun onCalcularRutaClick() {
        resolveSelectionsFromTextIfNeeded()

        val origen = _origenEstacion.value
        val destino = _destinoEstacion.value
        val transporte = _selectedTransportOption.value
        val optimizacion = _selectedOptimizationOption.value

        if (origen != null && destino != null) {
            println("🟢 Cálculo de ruta iniciado")
            println("➡️ Origen: ${origen.nombre}")
            println("🏁 Destino: ${destino.nombre}")
            println("🚇 Transporte: $transporte")
            println("⚙️ Optimización: $optimizacion")

            viewModelScope.launch {
                val estacionesIntermedias = simularRuta(origen, destino)
                val tiempoEstimado = "${(estacionesIntermedias.size + 1) * 2} min"

                _resultadoRuta.value = RutaResultado(
                    tiempoEstimado = tiempoEstimado,
                    estacionesIntermedias = estacionesIntermedias
                )

                println("✅ Ruta calculada: ${origen.nombre} → ${destino.nombre} ($tiempoEstimado)")
            }
        } else {
            println("No se puede calcular porque falta el origen o destino.")
        }
    }

    fun saveCurrentRoute() {
        resolveSelectionsFromTextIfNeeded()

        val origen = _origenEstacion.value
        val destino = _destinoEstacion.value
        val res = _resultadoRuta.value

        if (origen == null || destino == null || res == null) {
            println("No se puede guardar porque falta el origen, destino o resultado.")
            return
        }

        val minutos = res.tiempoEstimado.filter { it.isDigit() }.toIntOrNull() ?: 0
        val intermedias = res.estacionesIntermedias.joinToString("|") { it.nombre }

        val ruta = Ruta(
            id = 0,
            idEstacionOrigen = origen.id,
            nombreEstacionOrigen = origen.nombre,
            idEstacionDestino = destino.id,
            nombreEstacionDestino = destino.nombre,
            tiempoEstimadoMinutos = minutos,
            estacionesIntermedias = intermedias
        )

        viewModelScope.launch {
            rutaRepository.insertRoute(ruta)
            _isCurrentRouteFavorite.value = true
            println("💾 Ruta guardada: ${origen.nombre} → ${destino.nombre} (${minutos} min)")
        }
    }

    private fun checkIfCurrentRouteIsFavorite() {
        val o = _origenEstacion.value?.id
        val d = _destinoEstacion.value?.id
        if (o == null || d == null) {
            _isCurrentRouteFavorite.value = false
            return
        }
        viewModelScope.launch {
            rutaRepository.isRouteFavorite(o, d).collect { isFav ->
                _isCurrentRouteFavorite.value = isFav
            }
        }
    }

    fun clearSelections() {
        _origenEstacion.value = null
        _destinoEstacion.value = null
        _searchOrigenText.value = ""
        _searchDestinoText.value = ""
        _resultadoRuta.value = null
        _isCurrentRouteFavorite.value = false
    }






    // --- Simulación de algoritmo de rutas
    private fun simularRuta(origen: Estacion, destino: Estacion): List<Estacion> {
        val todas = _allEstaciones.value

        val indiceOrigen = todas.indexOfFirst { it.id == origen.id }
        val indiceDestino = todas.indexOfFirst { it.id == destino.id }

        if (indiceOrigen == -1 || indiceDestino == -1) return emptyList()

        val pasoInicio = minOf(indiceOrigen, indiceDestino)
        val pasoFin = maxOf(indiceOrigen, indiceDestino)

        return todas.subList(pasoInicio + 1, pasoFin)
    }

    // Esto deja un registro visible en el Logcat cada vez que el usuario presiona el botón.



    init {
        loadAllEstaciones()
        loadSavedRoutes()
    }

    private fun loadAllEstaciones() {
        viewModelScope.launch {
            estacionRepository.getEstaciones().collect { estaciones ->
                _allEstaciones.value = estaciones.sortedBy { it.id }
            }
        }
    }

    private fun loadSavedRoutes() {
        viewModelScope.launch {
            rutaRepository.allRoutes.collect { routes ->
                _savedRoutes.value = routes
                checkIfCurrentRouteIsFavorite()
            }
        }
    }


    // Funciones básicas de interacción (Medrano y Livia las completarán)


    fun setShowOriginPicker(show: Boolean) { _showOriginPicker.value = show }
    fun setShowDestinoPicker(show: Boolean) { _showDestinoPicker.value = show }
    suspend fun deleteRoute(ruta: Ruta) { /* ... */ }


    // --- Factory para el ViewModel ---
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