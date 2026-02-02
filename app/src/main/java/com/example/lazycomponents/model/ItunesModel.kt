package com.example.lazycomponents.model

import com.google.gson.annotations.SerializedName

data class ItunesResponse(
    val results: List<ItunesResult>
)

data class ItunesResult(
    @SerializedName("artworkUrl100") val artworkUrl: String?,
    @SerializedName("previewUrl") val previewUrl: String?,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?
)