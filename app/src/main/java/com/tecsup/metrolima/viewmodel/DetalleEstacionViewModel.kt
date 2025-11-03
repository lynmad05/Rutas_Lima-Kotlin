package com.tecsup.metrolima.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.EstacionExtendida
import com.tecsup.metrolima.repository.EstacionRepository
import com.tecsup.metrolima.repository.LineaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleEstacionViewModel(
    private val estacionRepository: EstacionRepository,
    private val estacionId: Int
) : ViewModel() {

    private val _estacion = MutableStateFlow<EstacionExtendida?>(null)
    val estacion: StateFlow<EstacionExtendida?> = _estacion.asStateFlow()

    private val _serviciosCercanos = MutableStateFlow<List<String>>(emptyList())
    val serviciosCercanos: StateFlow<List<String>> = _serviciosCercanos.asStateFlow()

    init {
        cargarEstacion()
        cargarServiciosCercanosSimulados()
    }

    private fun cargarEstacion() {
        viewModelScope.launch {
            try {
                val result = estacionRepository.getEstacionById(estacionId)
                _estacion.value = result
            } catch (e: Exception) {
                println("Error al cargar estación $estacionId: ${e.message}")

            }
        }
    }

    private fun cargarServiciosCercanosSimulados() {
        _serviciosCercanos.value = listOf(
            "Cajero Automático Global",
            "Farmacia 24 horas",
            "Parada de Bus (Metropolitano)",
            "Tienda de Conveniencia"
        )
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            _estacion.value?.let { currentEstacion ->
                val newFavoriteStatus = !currentEstacion.is_favorite

                estacionRepository.updateFavorite(currentEstacion.id, newFavoriteStatus)
                _estacion.update {
                    it?.copy(is_favorite = newFavoriteStatus)
                }

                println("Estación ${currentEstacion.nombre}: Favorito cambiado a $newFavoriteStatus")
            }
        }
    }

    companion object {
        fun provideFactory(context: Context, estacionId: Int): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(DetalleEstacionViewModel::class.java)) {
                        val db = MetroLimaDataBase.getDatabase(context)
                        val lineaRepo = LineaRepository(db.lineaDao())
                        val estacionRepo = EstacionRepository(db.estacionDao(), lineaRepo)
                        @Suppress("UNCHECKED_CAST")
                        return DetalleEstacionViewModel(estacionRepo, estacionId) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}