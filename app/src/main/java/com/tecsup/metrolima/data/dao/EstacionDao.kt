package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.Estacion
import kotlinx.coroutines.flow.Flow

@Dao
interface EstacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(estaciones: List<Estacion>)

    @Query("SELECT * FROM estaciones ORDER BY id ASC")
    fun getAllEstaciones(): Flow<List<Estacion>>


    @Query("SELECT COUNT(id) FROM estaciones")
    fun getCount(): Flow<Int>

    @Query("DELETE FROM estaciones")
    suspend fun deleteAllEstaciones()
}