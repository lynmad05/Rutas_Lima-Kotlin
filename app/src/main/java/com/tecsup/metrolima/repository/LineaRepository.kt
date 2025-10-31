package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.dao.LineaDao
import com.tecsup.metrolima.data.model.Linea
import kotlinx.coroutines.flow.Flow

class LineaRepository(
    private val lineaDao: LineaDao
) {

    suspend fun insertarLineas(lineas: List<Linea>) {
        lineaDao.insertAll(lineas)
    }

    fun getLineasLocales(): Flow<List<Linea>> {
        return lineaDao.getAll()
    }

    fun getLineaById(lineaId: Int): Flow<Linea?> {
        return lineaDao.getById(lineaId)
    }
}
