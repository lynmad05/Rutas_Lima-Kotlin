package com.tecsup.metrolima.data.model.remote

data class EstacionRemota(
    val id: Int,
    val nombre: String,
    val distrito: String,
    val lat: Double,
    val lon: Double,
    val linea_id: Int,
    val horario: String,
    val imagenCircular: String?,
    val is_favorite: Boolean = false
)
