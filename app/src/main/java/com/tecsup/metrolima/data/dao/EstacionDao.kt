package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.EstacionExtendida
import kotlinx.coroutines.flow.Flow

@Dao
interface EstacionDao {

    // Inserta o reemplaza todas las estaciones extendidas
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExtendidas(estaciones: List<EstacionExtendida>)

    // Retorna todas las estaciones con línea (para lista general)
    @Query("SELECT * FROM estaciones_extendidas")
    fun getAllEstacionesConLinea(): Flow<List<EstacionExtendida>>

    // Retorna estaciones por nombre de línea (usa tabla 'linea' según la entidad)
    @Query("""
        SELECT * FROM estaciones_extendidas
        WHERE linea_id IN (
            SELECT id FROM lineas WHERE nombre LIKE '%' || :nombreLinea || '%'
        )
    """)
    fun getEstacionesPorLinea(nombreLinea: String): Flow<List<EstacionExtendida>>

    // Retorna estación por ID (para detalle)
    @Query("SELECT * FROM estaciones_extendidas WHERE id = :id LIMIT 1")
    suspend fun getEstacionConLineaById(id: Int): EstacionExtendida

    // Actualiza el estado de favorito
    @Query("UPDATE estaciones_extendidas SET is_favorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: Int, isFavorite: Boolean)

    // Retorna todas las estaciones por línea (para mapa)
    @Query("SELECT * FROM estaciones_extendidas WHERE linea_id = :id ORDER BY id ASC")
    fun getEstacionesPorLineaId(id: Int): Flow<List<EstacionExtendida>>
}
