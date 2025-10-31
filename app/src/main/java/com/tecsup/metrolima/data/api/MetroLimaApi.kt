package com.tecsup.metrolima.data.api

import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Linea
import retrofit2.http.GET

interface MetroLimaApi {
    @GET("81fed639-e328-4e03-a4c6-086a6780defc")
    suspend fun getEstaciones(): List<Estacion>

    @GET("81fed639-e328-4e03-a4c6-086a6780defc")
    suspend fun getLineas(): List<Linea>
}