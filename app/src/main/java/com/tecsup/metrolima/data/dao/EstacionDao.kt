package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.metrolima.data.model.Estacion

@Dao
interface EstacionDao{
    //Inserta la lista de estaciones
    @Insert
    suspend fun insertarEstaciones(estaciones: List<Estacion>)

    //Obtiene todas las estaciones que se almacenan
    @Query("SELECT * FROM estaciones")
    suspend fun getAllEstaciones(): List<Estacion>
}