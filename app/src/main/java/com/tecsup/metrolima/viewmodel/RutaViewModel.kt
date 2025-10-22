package com.tecsup.metrolima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
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

    fun onOrigenSelected(estacion: Estacion) { /* ... */ }
    fun onDestinoSelected(estacion: Estacion) { /* ... */ }
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