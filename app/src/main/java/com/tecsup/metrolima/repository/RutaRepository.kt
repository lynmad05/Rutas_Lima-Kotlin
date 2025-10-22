package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.dao.RutaDao
import com.tecsup.metrolima.data.model.Ruta
import kotlinx.coroutines.flow.Flow

class RutaRepository(private val rutaDao: RutaDao) {

    val allRoutes: Flow<List<Ruta>> = rutaDao.getAll()

    suspend fun insertRoute(ruta: Ruta) {
        rutaDao.insert(ruta)
    }

    suspend fun deleteRoute(ruta: Ruta) {
        rutaDao.delete(ruta)
    }

    fun isRouteFavorite(origenId: Int, destinoId: Int): Flow<Boolean> {
        return rutaDao.isRouteFavorite(origenId, destinoId)
    }
}