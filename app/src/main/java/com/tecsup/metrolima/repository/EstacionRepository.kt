package com.tecsup.metrolima.repository

import com.tecsup.metrolima.data.dao.EstacionDao
import com.tecsup.metrolima.data.model.Estacion

class EstacionRepository(
    private val estacionDao: EstacionDao
){
    //Obtiene todas las estaciones almacenadas
    suspend fun getEstaciones(): List<Estacion> = estacionDao.getAllEstaciones()

    //Inserta una lista de estaciones iniciales
    suspend fun insertarEstaciones(estaciones: List<Estacion>){
        estacionDao.insertarEstaciones(estaciones)
    }
}
