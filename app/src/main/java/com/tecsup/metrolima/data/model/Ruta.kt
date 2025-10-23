package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ruta")
data class Ruta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idEstacionOrigen: Int,
    val nombreEstacionOrigen: String,
    val idEstacionDestino: Int,
    val nombreEstacionDestino: String,
    val tiempoEstimadoMinutos: Int,
    val estacionesIntermedias: String
)