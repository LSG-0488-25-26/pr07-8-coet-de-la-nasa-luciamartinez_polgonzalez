package com.example.lazycomponents.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.lazycomponents.api.Repository
import com.example.lazycomponents.local.KaraokeDatabase
import com.example.lazycomponents.model.ItunesResponse
import com.example.lazycomponents.model.ItunesResult
import com.example.lazycomponents.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class KaraokeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository()

    private val database = KaraokeDatabase.getDatabase(application)
    private val dao = database.songsDao()

    private val _topSongs = MutableLiveData<List<ItunesResult>>(emptyList())
    val topSongs: LiveData<List<ItunesResult>> = _topSongs

    private val _searchText = MutableLiveData<String>("")
    val searchText: LiveData<String> = _searchText

    init {
        loadTop10()
    }

    private fun loadTop10() {
        viewModelScope.launch (Dispatchers.IO) {
            try{
                val response = repository.itunesApi.searchMusic(term = "hits", limit = 10)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        _topSongs.value = response.body()?.results ?: emptyList()
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    fun getAllSongs(): LiveData<List<Song>> = dao.getAllSongs()

    fun deleteSong(song: Song) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteSong(song)
            if (song.artist == currentArtist && song.title == currentTitle) {
                withContext(Dispatchers.Main) {
                    _isFavorite.value = false
                }
            }
        }
    }

    fun onSearchTextChange(text: String){
        _searchText.value = text
    }

    private val _lyrics = MutableLiveData<String>("Busca una canción...")
    val lyrics: LiveData<String> = _lyrics

    private val _coverUrl = MutableLiveData<String?>(null)
    val coverUrl: LiveData<String?> = _coverUrl

    private val _audioUrl = MutableLiveData<String?>(null)
    val audioUrl: LiveData<String?> = _audioUrl

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var currentArtist: String = ""
    private var currentTitle: String = ""

    fun searchLyrics(artist: String, title: String) {
        _isLoading.value = true
        _lyrics.value = "Buscando..."
        _coverUrl.value = null
        _audioUrl.value = null
        _isFavorite.value = false

        currentArtist = artist.trim()
        currentTitle = title.trim()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val localSong = dao.getSongByArtistAndTitle(currentArtist, currentTitle)

                if (localSong != null) {
                    withContext(Dispatchers.Main) {
                        _isLoading.value = false
                        _lyrics.value = localSong.lyrics
                        _coverUrl.value = localSong.coverUrl
                        _audioUrl.value = localSong.audioUrl
                        _isFavorite.value = true
                        mostrarToast("Cargado desde Favoritos (Offline)")
                    }
                } else {
                    fetchFromNetwork(currentArtist, currentTitle)
                }
            } catch (e: Exception) {
                fetchFromNetwork(currentArtist, currentTitle)
            }
        }
    }

    private suspend fun fetchFromNetwork(artist: String, title: String) {
        try {
            val lyricsResponse = try { repository.getLyrics(artist, title) } catch (e: Exception) { null }
            val musicData = try { repository.getMusicData(artist, title) } catch (e: Exception) { null }

            withContext(Dispatchers.Main) {
                _isLoading.value = false

                _coverUrl.value = musicData?.coverUrl
                _audioUrl.value = musicData?.audioUrl

                if (lyricsResponse != null && lyricsResponse.isSuccessful) {
                    _lyrics.value = lyricsResponse.body()?.lyrics ?: "Letra no disponible"
                } else {
                    _lyrics.value = "No se encontró la letra. (Error 404)"
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                _isLoading.value = false
                _lyrics.value = "Error de red: ${e.message}"
            }
        }
    }

    fun toggleFavorite() {
        if (currentArtist.isBlank() || currentTitle.isBlank()) return
        val currentLyrics = _lyrics.value ?: ""
        if (currentLyrics.startsWith("Busca") || currentLyrics.startsWith("Error") || currentLyrics.startsWith("No se")) {
            mostrarToast("No puedes guardar esto")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val existingSong = dao.getSongByArtistAndTitle(currentArtist, currentTitle)

            if (existingSong != null) {
               dao.deleteSong(existingSong)
                withContext(Dispatchers.Main) {
                    _isFavorite.value = false
                    mostrarToast("Eliminado de Favoritos ")
                }
            } else {
               val newSong = Song(
                    artist = currentArtist,
                    title = currentTitle,
                    lyrics = currentLyrics,
                    coverUrl = _coverUrl.value,
                    audioUrl = _audioUrl.value,
                    isFavorite = true
                )
                dao.insertSong(newSong)
                withContext(Dispatchers.Main) {
                    _isFavorite.value = true
                    mostrarToast("¡Guardado en Favoritos!")
                }
            }
        }
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(getApplication(), mensaje, Toast.LENGTH_SHORT).show()
    }
}