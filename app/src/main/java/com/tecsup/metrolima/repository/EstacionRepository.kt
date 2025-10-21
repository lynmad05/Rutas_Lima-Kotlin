package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.api.RetrofitInstance
import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.Estacion

class EstacionRepository(private val estacionDao: EstacionDao) {

    suspend fun insertarEstaciones(estaciones: List<Estacion>) {
        estacionDao.insertAll(estaciones)
    }

    suspend fun getEstaciones(): List<Estacion> {
        return estacionDao.getAllEstaciones()
    }

    suspend fun getStationCount(): Int {
        return estacionDao.getCount()
    }

    suspend fun getEstacionesRemotas(): List<Estacion>{
        return RetrofitInstance.api.getEstaciones()
    }
}