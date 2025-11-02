package com.tecsup.metrolima.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tecsup.metrolima.data.model.Linea
import kotlinx.coroutines.flow.Flow

@Dao
interface LineaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(lineas: List<Linea>)

    @Query("SELECT * FROM lineas")
    fun getAll(): Flow<List<Linea>>

    @Query("DELETE FROM lineas")
    suspend fun deleteAll()
}
