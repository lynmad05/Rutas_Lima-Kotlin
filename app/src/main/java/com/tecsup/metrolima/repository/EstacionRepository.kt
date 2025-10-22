package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.api.RetrofitInstance
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.Estacion
import kotlinx.coroutines.flow.Flow

class EstacionRepository(private val estacionDao: EstacionDao) {

    suspend fun insertarEstaciones(estaciones: List<Estacion>) {
        estacionDao.insertAll(estaciones)
    }

    fun getEstaciones(): Flow<List<Estacion>> {
        return estacionDao.getAllEstaciones()
    }

    fun getStationCount(): Flow<Int> {
        return estacionDao.getCount()
    }

    suspend fun getEstacionesRemotas(): List<Estacion>{
        return RetrofitInstance.api.getEstaciones()
    }
    suspend fun deleteAllEstaciones() {
        estacionDao.deleteAllEstaciones()
    }
}