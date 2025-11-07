package com.tecsup.metrolima.data.api

import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Linea
import retrofit2.http.GET

interface MetroLimaApi {

    // Estaciones Mocki v2
    @GET("bbc5cff0-5f7b-498e-8ca5-56059419a478")
    suspend fun getEstaciones(): List<Estacion>

    // Líneas Mocki
    @GET("0026bb55-e64a-4bc0-ab3b-c126ecf4ab61")
    suspend fun getLineas(): List<Linea>
}