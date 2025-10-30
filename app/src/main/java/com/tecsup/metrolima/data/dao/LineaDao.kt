package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.Linea

@Dao
interface LineaDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lineas: List<Linea>)

    @Query("SELECT * FROM linea")
    suspend fun getAll(): List<Linea>

    @Query("SELECT * FROM linea WHERE id = :lineaId")
    suspend fun getById(lineaId: Int): Linea?

}