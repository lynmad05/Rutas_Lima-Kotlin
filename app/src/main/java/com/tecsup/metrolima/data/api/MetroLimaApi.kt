package com.tecsup.metrolima.data.api

import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Linea
import retrofit2.http.GET

interface MetroLimaApi {

    // Estaciones Mocki v2
    @GET("8566829c-c1c4-4e40-bda8-c0b1cd1ff533")
    suspend fun getEstaciones(): List<Estacion>

    // Líneas Mocki
    @GET("0026bb55-e64a-4bc0-ab3b-c126ecf4ab61")
    suspend fun getLineas(): List<Linea>
}