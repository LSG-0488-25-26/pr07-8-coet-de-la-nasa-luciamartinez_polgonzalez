package com.example.lazycomponents.api

import com.example.lazycomponents.model.LyricsResponse
import retrofit2.Response

class Repository {
    private val lyricsApi = LyricsApi.create()
    private val itunesApi = ItunesApi.create()

    suspend fun getLyrics(artist: String, title: String): Response<LyricsResponse> {
        return lyricsApi.getLyrics(artist, title)
    }

    suspend fun getCover(artist: String, title: String): String? {
        return try {
            val response = itunesApi.searchMusic(term = "$artist $title")
            if (response.isSuccessful) {
                response.body()?.results?.firstOrNull()?.artworkUrl?.replace("100x100", "600x600")
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

