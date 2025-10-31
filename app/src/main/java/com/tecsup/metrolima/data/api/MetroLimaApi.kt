package com.tecsup.metrolima.data.api

import com.tecsup.metrolima.data.model.Estacion
import com.tecsup.metrolima.data.model.Linea
import retrofit2.http.GET

interface MetroLimaApi {
    @GET("81fed639-e328-4e03-a4c6-086a6780defc")
    suspend fun getEstaciones(): List<Estacion>

    @GET("fcedd880-566d-4746-858e-f91e612d764f")
    suspend fun getLineas(): List<Linea>
}