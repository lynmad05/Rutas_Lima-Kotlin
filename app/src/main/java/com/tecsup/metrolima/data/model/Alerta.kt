package com.tecsup.metrolima.data.model


data class Alerta(
    val id: Int,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val tipo: String            // Ej: "Retraso", "Cierre", "Info"
)