package com.example.lazycomponents.api

import com.example.lazycomponents.model.LyricsResponse
import com.example.lazycomponents.model.ItunesResponse
import retrofit2.Response

// 1. Definimos esta clase AQUÍ, fuera de la clase Repository
data class MusicData(val coverUrl: String?, val audioUrl: String?)

class Repository {
    private val lyricsApi = LyricsApi.create()
    private val itunesApi = ItunesApi.create()

    suspend fun getLyrics(artist: String, title: String): Response<LyricsResponse> {
        return lyricsApi.getLyrics(artist, title)
    }

    suspend fun getMusicData(artist: String, title: String): MusicData? {
        return try {
            val response = itunesApi.searchMusic(term = "$artist $title")
            if (response.isSuccessful && !response.body()?.results.isNullOrEmpty()) {
                val result = response.body()?.results!!.first()

                MusicData(
                    coverUrl = result.artworkUrl?.replace("100x100", "600x600"),
                    audioUrl = result.previewUrl
                )
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}