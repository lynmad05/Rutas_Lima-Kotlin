package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.EstacionExtendida
import kotlinx.coroutines.flow.Flow

@Dao
interface EstacionDao {

    // =========================================================
    // 🟢 Métodos antiguos - Compatibilidad con Estacion original
    // =========================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(estaciones: List<Estacion>)

    @Query("SELECT * FROM estaciones ORDER BY id ASC")
    fun getAllEstaciones(): Flow<List<Estacion>>

    @Query("SELECT COUNT(id) FROM estaciones")
    fun getCount(): Flow<Int>

    @Query("DELETE FROM estaciones")
    suspend fun deleteAllEstaciones()

    @Query("""
    SELECT e.*, l.nombre AS lineaNombre, l.color AS lineaColor 
    FROM estaciones_extendidas e 
    INNER JOIN linea l ON e.linea_id = l.id 
    WHERE l.nombre = :nombreLinea 
    ORDER BY e.nombre ASC
""")
    fun getEstacionesPorLinea(nombreLinea: String): Flow<List<EstacionExtendida>>



    // =========================================================
    // 🔵 Métodos nuevos - Basados en EstacionExtendida y Línea
    // =========================================================

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExtendidas(estaciones: List<EstacionExtendida>)

    @Query("""
        SELECT e.*, l.nombre AS lineaNombre, l.color AS lineaColor
        FROM estaciones_extendidas e
        INNER JOIN linea l ON e.linea_id = l.id 
        ORDER BY e.id ASC
    """)
    fun getAllEstacionesConLinea(): Flow<List<EstacionExtendida>>

    @Query("DELETE FROM estaciones_extendidas")
    suspend fun deleteAllEstacionesExtendidas()
}
