package com.example.lazycomponents.model

import com.google.gson.annotations.SerializedName

data class LyricsResponse(
    @SerializedName("lyrics") val lyrics: String?
)

data class Song(
    val artist: String,
    val title: String,
    val lyrics: String,
    val isFavourite: Boolean = false
)