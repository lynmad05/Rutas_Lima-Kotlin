package com.tecsup.metrolima.data.model

data class Ruta(
    val id: Int,
    val origen: Estacion,
    val destino: Estacion,
    val tiempoEstimado: Int,
    val pasos: List<String>
)