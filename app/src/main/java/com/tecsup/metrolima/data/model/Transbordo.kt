package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Representa un punto de transbordo entre dos estaciones.
 * Cada registro indica que puedes hacer conexión entre una estación A y una estación B.
 */
@Entity(
    tableName = "transbordos",
    foreignKeys = [
        ForeignKey(
            entity = EstacionExtendida::class,
            parentColumns = ["id"],
            childColumns = ["estacion_origen_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EstacionExtendida::class,
            parentColumns = ["id"],
            childColumns = ["estacion_destino_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["estacion_origen_id"]), Index(value = ["estacion_destino_id"])]
)
data class Transbordo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val estacion_origen_id: Int,
    val estacion_destino_id: Int,

    // 🔹 Opcional: descripción de la conexión
    val descripcion: String? = null
)
