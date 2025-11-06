package com.tecsup.metrolima.data.model

import com.google.android.gms.maps.model.LatLng

data class LineaConPuntos(
    val id: Int,
    val nombre: String,
    val color: Long,
    val puntos: List<LatLng>
)