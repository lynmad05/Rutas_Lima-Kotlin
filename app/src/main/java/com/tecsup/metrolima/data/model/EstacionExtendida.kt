package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Entidad extendida de Estación.
 * Incluye relación con la entidad Linea y coordenadas (lat/lon),
 * además del campo de favorito para el detalle.
 */
@Parcelize
@Entity(
    tableName = "estaciones_extendidas",
    foreignKeys = [
        ForeignKey(
            entity = Linea::class,
            parentColumns = ["id"],
            childColumns = ["linea_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EstacionExtendida(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val distrito: String,
    val lat: Double,
    val lon: Double,
    val linea_id: Int,  // Relación con la tabla Linea
    val horario: String,
    val imagenCircular: String? = null,

    // Recurso opcional local (para modo offline o placeholder)
    val imagenCircularResId: Int = 0,

    // Campo nuevo: estado de favorito (para DetalleEstacionViewModel)
    val is_favorite: Boolean = false
) : Parcelable