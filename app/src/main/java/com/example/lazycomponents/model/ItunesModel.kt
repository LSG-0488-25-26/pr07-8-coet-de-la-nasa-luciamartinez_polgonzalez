package com.example.lazycomponents.model

import com.google.gson.annotations.SerializedName

data class ItunesResponse(
    val results: List<ItunesResult>
)

data class ItunesResult(
    @SerializedName("artworkUrl100") val artworkUrl: String? // La URL de la foto
)