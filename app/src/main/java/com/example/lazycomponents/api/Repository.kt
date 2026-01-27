package com.example.lazycomponents.api

import com.example.lazycomponents.model.LyricsResponse
import retrofit2.Response

class Repository {
    private val api = LyricsApi.create()

    suspend fun getLyrics(artist: String, title: String): Response<LyricsResponse> {
        return api.getLyrics(artist, title)
    }
}

