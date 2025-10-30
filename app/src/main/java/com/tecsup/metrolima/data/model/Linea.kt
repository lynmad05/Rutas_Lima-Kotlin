package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lineas")
data class Linea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val colorHex: String,
    val estado: String
)