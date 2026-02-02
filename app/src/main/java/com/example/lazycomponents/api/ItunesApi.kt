package com.example.lazycomponents.api

import com.example.lazycomponents.model.ItunesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {

    @GET("search")
    suspend fun searchMusic(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("limit") limit: Int = 1,
        @Query("entity") entity: String = "song"
    ): Response<ItunesResponse>

    companion object {
        private const val BASE_URL = "https://itunes.apple.com/"

        fun create(): ItunesApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesApi::class.java)
        }
    }
}