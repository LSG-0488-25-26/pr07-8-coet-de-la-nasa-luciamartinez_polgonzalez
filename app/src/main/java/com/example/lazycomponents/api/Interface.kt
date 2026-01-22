package com.example.lazycomponents.api

import retrofit2.http.GET

interface LetrasServicioAPI {
    @GET("")
    suspend fun getLetras(): Response<LetrasRespuesta>
}