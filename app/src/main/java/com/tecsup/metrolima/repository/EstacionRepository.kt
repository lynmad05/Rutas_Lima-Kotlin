package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.api.RetrofitInstance
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.EstacionExtendida
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.map
class EstacionRepository(
    private val estacionDao: EstacionDao,
    private val lineaRepository: LineaRepository
) {

    // Inserta todas las estaciones extendidas
    suspend fun insertarEstacionesExtendidas(estaciones: List<EstacionExtendida>) {
        try {
            estacionDao.insertAllExtendidas(estaciones)
            println("Se insertaron ${estaciones.size} estaciones en la base local.")
        } catch (e: Exception) {
            println("Error al insertar estaciones: ${e.message}")
        }
    }

    // Retorna todas las estaciones con su línea asociada
    fun getAllEstacionesConLinea(): Flow<List<EstacionExtendida>> =
        estacionDao.getAllEstacionesConLinea()

    // Retorna estaciones filtradas por nombre de línea

    fun getEstacionesPorLinea(nombreLinea: String): Flow<List<EstacionExtendida>> =
        estacionDao.getEstacionesPorLinea(nombreLinea)

    // Retorna una estación específica por ID
    suspend fun getEstacionById(id: Int): EstacionExtendida =
        estacionDao.getEstacionConLineaById(id)

    // Actualiza el estado de favorito
    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        estacionDao.updateFavoriteStatus(id, isFavorite)
        println("Estación $id actualizada como favorita = $isFavorite")
    }

    // Retorna coordenadas (lat/lon) para trazado del mapa
    fun getCoordenadasPorLinea(id: Int): Flow<List<LatLng>> =
        estacionDao.getEstacionesPorLineaId(id).map { coords ->
            coords.map { LatLng(it.lat, it.lon) }
        }


    // Obtiene las estaciones remotas desde el endpoint Mocki
    suspend fun getEstacionesRemotas(): List<EstacionExtendida> {
        println("Solicitando estaciones desde Mocki...")

        return try {
            val estacionesRemotas = RetrofitInstance.api.getEstaciones()
            val lineasLocales = lineaRepository.getLineasLocales().first()

            println("Estaciones remotas recibidas: ${estacionesRemotas.size}")
            println("Lineas locales disponibles: ${lineasLocales.size}")

            estacionesRemotas.map { remote ->
                val lineaEncontrada = lineasLocales.find { linea ->
                    remote.linea.contains(linea.nombre, ignoreCase = true)
                }
                EstacionExtendida(
                    id = 0,
                    nombre = remote.nombre,
                    distrito = remote.distrito,
                    lat = remote.latitud,
                    lon = remote.longitud,
                    linea_id = lineaEncontrada?.id ?: 0,
                    horario = remote.horario,
                    imagenCircular = remote.imagenCircular,
                    is_favorite = false
                )
            }
        } catch (e: Exception) {
            println("Error al obtener estaciones remotas: ${e.message}")
            emptyList()
        }
    }

    // Sincroniza las líneas y estaciones desde Mocki
    suspend fun fetchAndSaveAllData() {
        try {
            println("Sincronizando datos iniciales desde Mocki...")
            fetchAndSaveLineas()

            val estacionesMapeadas = getEstacionesRemotas()
            if (estacionesMapeadas.isNotEmpty()) {
                insertarEstacionesExtendidas(estacionesMapeadas)
                println("Estaciones cargadas y guardadas correctamente (${estacionesMapeadas.size}).")
            } else {
                println("⚠No se encontraron estaciones para guardar.")
            }

        } catch (e: Exception) {
            println("Error general al sincronizar datos: ${e.message}")
            e.printStackTrace()
        }
    }

    // Descarga y guarda líneas desde Mocki
    private suspend fun fetchAndSaveLineas() {
        println("Intentando cargar líneas desde Mocki...")

        val lineasRemotas = try {
            val response = RetrofitInstance.api.getLineas()
            println("Respuesta Mocki (líneas): ${response}")
            response
        } catch (e: Exception) {
            println("Error de red al obtener líneas: ${e.message}")
            emptyList()
        }

        if (lineasRemotas.isNotEmpty()) {
            lineaRepository.insertarLineas(lineasRemotas)
            println("Líneas iniciales cargadas y guardadas: ${lineasRemotas.size} líneas.")
        } else {
            println("No se encontraron líneas remotas para guardar.")
        }
    }
}
