package com.example.lazycomponents.model

import com.google.gson.annotations.SerializedName
import kotlin.time.Duration

data class LyricsResponse(
    @SerializedName("plainLyrics") val lyrics: String?
)

data class Song(
    val artist: String,
    val title: String,
    val lyrics: String,
    val coverUrl: String? = null,
    val isFavourite: Boolean = false
)