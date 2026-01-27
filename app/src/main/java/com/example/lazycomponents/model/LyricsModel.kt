package com.example.lazycomponents.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlin.time.Duration

data class LyricsResponse(
    @SerializedName("plainLyrics") val lyrics: String?
)

@Entity(tableName = "songs_table")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    val artist: String,
    val title: String,
    val lyrics: String,

    val coverUrl: String? = null,
    val audioUrl: String? = null,

    val isFavorite: Boolean = false
)