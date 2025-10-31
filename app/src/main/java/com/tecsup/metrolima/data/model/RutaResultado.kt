package com.tecsup.metrolima.data.model

data class RutaResultado(
    val tiempoEstimado: String,
    val estacionesIntermedias: List<EstacionExtendida>
)