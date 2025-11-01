package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.api.RetrofitInstance
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.EstacionExtendida
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class EstacionRepository(
    private val estacionDao: EstacionDao,
    private val lineaRepository: LineaRepository

) {


    suspend fun insertarEstacionesExtendidas(estaciones: List<EstacionExtendida>) {
        estacionDao.insertAllExtendidas(estaciones)
    }

    fun getAllEstacionesConLinea(): Flow<List<EstacionExtendida>> {
        return estacionDao.getAllEstacionesConLinea()
    }
    fun getEstacionesPorLinea(nombreLinea: String): Flow<List<EstacionExtendida>> {
        return estacionDao.getEstacionesPorLinea(nombreLinea)
    }



    suspend fun getEstacionesRemotas(): List<EstacionExtendida> {
        val estacionesRemotas = RetrofitInstance.api.getEstaciones()
        val lineasLocales = lineaRepository.getLineasLocales().first()
        return estacionesRemotas.map { remoteEstacion ->
            val lineaEncontrada = lineasLocales.find { linea ->
                remoteEstacion.linea.contains(linea.nombre, ignoreCase = true)
            }
            EstacionExtendida(
                id = 0,
                nombre = remoteEstacion.nombre,
                distrito = remoteEstacion.distrito,
                lat = remoteEstacion.latitud,
                lon = remoteEstacion.longitud,
                linea_id = lineaEncontrada?.id ?: 0,
                horario = remoteEstacion.horario,
                imagenCircular = remoteEstacion.imagenCircular
            )
        }
    }

    suspend fun deleteAllEstaciones() {
        estacionDao.deleteAllEstacionesExtendidas()
    }

    suspend fun fetchAndSaveAllData() {
        try {
            fetchAndSaveLineas()
            val estacionesMapeadas = getEstacionesRemotas()
            if (estacionesMapeadas.isNotEmpty()) {
                insertarEstacionesExtendidas(estacionesMapeadas)
                println("Estaciones iniciales cargadas y guardadas: ${estacionesMapeadas.size} estaciones.")
            } else {
                println("No se encontraron estaciones remotas para guardar.")
            }

        } catch (e: Exception) {
            println("Error general al cargar o guardar datos iniciales: ${e.message}")
            e.printStackTrace()
        }
    }

    private suspend fun fetchAndSaveLineas() {
        val lineasRemotas = try {
            RetrofitInstance.api.getLineas()
        } catch (e: Exception) {
            println(" Error de red al obtener líneas: ${e.message}")
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

