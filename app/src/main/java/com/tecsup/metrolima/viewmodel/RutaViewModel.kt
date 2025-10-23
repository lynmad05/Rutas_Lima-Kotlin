package com.tecsup.metrolima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.room.Query
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Ruta
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

    fun onCalcularRutaClick(){
        val origen = _origenEstacion.value
        val destino = _destinoEstacion.value
        val transporte = _selectedTransportOption.value
        val optimizacion = _selectedOptimizationOption.value

        //Solo visual en  el logcat
        if (origen != null && destino != null){
            println("🟢 Cálculo de ruta iniciado")
            println("➡️ Origen: ${origen.nombre}")
            println("🏁 Destino: ${destino.nombre}")
            println("🚇 Transporte: $transporte")
            println("⚙️ Optimización: $optimizacion")

            // Aquí Marlon implementará la lógica real del cálculo

        } else {
            println("No se puede calcular: falta origen o destino.")
        }
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
    fun clearSelections() { /* ... */ }
    private fun checkIfCurrentRouteIsFavorite() { /* ... */ }
    fun saveCurrentRoute() { /* ... */ }
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