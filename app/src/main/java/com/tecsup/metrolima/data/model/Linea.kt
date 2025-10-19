package com.tecsup.metrolima.data.model

data class Linea(
    val id: Int,
    val nombre: String,
    val colorHex: String,
    val estaciones: List<Estacion>
)