package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estaciones")
data class Estacion(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double
)