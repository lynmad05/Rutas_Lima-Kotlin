package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lineas")
data class Linea(
    @PrimaryKey val id: Int,
    val nombre: String,
    val color: String,
    val estado: String
)