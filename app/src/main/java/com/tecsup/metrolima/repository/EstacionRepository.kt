package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.db.EstacionDao
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
}