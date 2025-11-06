package com.tecsup.metrolima.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.repository.EstacionRepository
import com.tecsup.metrolima.repository.LineaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MapaLineaViewModel(
    private val estacionRepository: EstacionRepository,
    private val lineaId: Int
) : ViewModel() {

    private val _coordenadasLinea = MutableStateFlow<List<LatLng>>(emptyList())
    val coordenadasLinea: StateFlow<List<LatLng>> = _coordenadasLinea.asStateFlow()

    init {
        cargarCoordenadasLinea()
    }

    private fun cargarCoordenadasLinea() {
        viewModelScope.launch {
            estacionRepository.getCoordenadasPorLinea(lineaId)
                .catch { e ->
                    println("Error al cargar coordenadas para la línea $lineaId: ${e.message}")
                }
                .collect { listaLatLng ->
                    _coordenadasLinea.value = listaLatLng
                    println("Línea $lineaId cargada con ${listaLatLng.size} puntos.")
                }
        }
    }

    companion object {
        fun provideFactory(context: Context, lineaId: Int): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(MapaLineaViewModel::class.java)) {
                        val db = MetroLimaDataBase.getDatabase(context)
                        val lineaRepo = LineaRepository(db.lineaDao())
                        val estacionRepo = EstacionRepository(db.estacionDao(), lineaRepo)
                        @Suppress("UNCHECKED_CAST")
                        return MapaLineaViewModel(estacionRepo, lineaId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}