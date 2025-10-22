package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.Ruta
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaDao {

    @Query("SELECT * FROM ruta ORDER BY id DESC")
    fun getAll(): Flow<List<Ruta>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ruta: Ruta)

    @Delete
    suspend fun delete(ruta: Ruta)

    @Query("SELECT EXISTS(SELECT 1 FROM ruta WHERE idEstacionOrigen = :origenId AND idEstacionDestino = :destinoId LIMIT 1)")
    fun isRouteFavorite(origenId: Int, destinoId: Int): Flow<Boolean>
}