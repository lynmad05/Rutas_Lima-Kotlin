package com.tecsup.metrolima.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.repository.EstacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapaGeneralViewModel(
    private val estacionRepository: EstacionRepository
) : ViewModel() {

    private val _lineas = MutableStateFlow<List<LineaConPuntos>>(emptyList())
    val lineas: StateFlow<List<LineaConPuntos>> = _lineas

    init {
        cargarLineasYCoordenadas()
    }

    private fun cargarLineasYCoordenadas() {
        viewModelScope.launch {
            try {
                val todasLasLineas = listOf(
                    LineaConPuntos(
                        id = 1,
                        nombre = "Línea 1",
                        color = 0xFF00C853,
                        puntos = estacionRepository.getCoordenadasPorLinea(1).first()
                    ),
                    LineaConPuntos(
                        id = 2,
                        nombre = "Línea 2",
                        color = 0xFFFFEB3B,
                        puntos = estacionRepository.getCoordenadasPorLinea(2).first()
                    ),
                    LineaConPuntos(
                        id = 3,
                        nombre = "Línea 3",
                        color = 0xFF2196F3,
                        puntos = estacionRepository.getCoordenadasPorLinea(3).first()
                    )
                )
                _lineas.value = todasLasLineas
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    data class LineaConPuntos(
        val id: Int,
        val nombre: String,
        val color: Long,
        val puntos: List<LatLng>
    )

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val db = MetroLimaDataBase.getDatabase(context)
                    val estacionRepo = EstacionRepository(
                        db.estacionDao(),
                        com.tecsup.metrolima.repository.LineaRepository(db.lineaDao())
                    )
                    @Suppress("UNCHECKED_CAST")
                    return MapaGeneralViewModel(estacionRepo) as T
                }
            }
        }
    }
}