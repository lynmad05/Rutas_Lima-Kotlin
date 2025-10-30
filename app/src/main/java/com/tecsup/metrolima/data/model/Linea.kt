package com.tecsup.metrolima.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "linea")
data class Linea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val color: String,
    val estado: String
)