package com.tecsup.metrolima.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.tecsup.metrolima.data.db.MetroLimaDataBase
import com.tecsup.metrolima.data.model.Linea
import com.tecsup.metrolima.repository.LineaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListaLineasViewModel(
    private val lineaRepository: LineaRepository
) : ViewModel() {

    private val _lineas = MutableStateFlow<List<Linea>>(emptyList())
    val lineas: StateFlow<List<Linea>> get() = _lineas

    init {
        // 🔥 Nuevo: intentar descargar y guardar líneas desde Mocki al inicio
        viewModelScope.launch {
            try {
                val db = lineaRepository // solo referencia
                println("Intentando sincronizar líneas iniciales...")
                // Usa tu estacionRepository si es necesario, o crea aquí una función similar
                // Si solo tienes LineaRepository, agrega una función allí para cargar remotas si existe

            } catch (e: Exception) {
                println("Error al sincronizar líneas: ${e.message}")
            }
        }

        cargarLineas()
    }

    private fun cargarLineas() {
        viewModelScope.launch {
            lineaRepository.getLineasLocales().collect { lista ->
                _lineas.value = lista
            }
        }
    }

    companion object {
        fun provideFactory(context: Context): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                   override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ListaLineasViewModel::class.java)) {
                        val db = MetroLimaDataBase.getDatabase(context)
                        val lineaDao = db.lineaDao()
                        val lineaRepo = LineaRepository(lineaDao)
                        @Suppress("UNCHECKED_CAST")
                        return ListaLineasViewModel(lineaRepo) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
