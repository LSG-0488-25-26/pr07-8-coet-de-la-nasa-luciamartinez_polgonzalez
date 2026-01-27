package com.example.lazycomponents.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lazycomponents.model.Song

@Dao
interface SongsDao {

    @Query("SELECT * FROM songs_table ORDER BY id DESC")
    fun getAllSongs(): LiveData<List<Song>>

    @Query("SELECT * FROM songs_table WHERE artist = :artist AND title = :title LIMIT 1")
    suspend fun getSongByArtistAndTitle(artist: String, title: String): Song?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)
}