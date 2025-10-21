package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "estaciones")
data class Estacion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val distrito: String,
    val latitud: Double,
    val longitud: Double,
    val linea: String,
    val horario: String,
    val imagenCircular: String? = null,
    val imagenCircularResId: Int = 0
) : Parcelable