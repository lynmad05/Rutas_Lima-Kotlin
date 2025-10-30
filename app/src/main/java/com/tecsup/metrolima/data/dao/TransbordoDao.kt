package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.Transbordo
import kotlinx.coroutines.flow.Flow

@Dao
interface TransbordoDao {

    // 🔹 Insertar un solo transbordo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transbordo: Transbordo)

    // 🔹 Insertar varios transbordos a la vez
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transbordos: List<Transbordo>)

    // 🔹 Obtener todos los transbordos registrados
    @Query("SELECT * FROM transbordos ORDER BY id ASC")
    fun getAllTransbordos(): Flow<List<Transbordo>>

    // 🔹 Obtener transbordos desde una estación específica
    @Query("""
        SELECT * FROM transbordos
        WHERE estacion_origen_id = :estacionId
           OR estacion_destino_id = :estacionId
        ORDER BY id ASC
    """)
    fun getTransbordosByEstacion(estacionId: Int): Flow<List<Transbordo>>

    // 🔹 Borrar todos los transbordos
    @Query("DELETE FROM transbordos")
    suspend fun deleteAll()
}
