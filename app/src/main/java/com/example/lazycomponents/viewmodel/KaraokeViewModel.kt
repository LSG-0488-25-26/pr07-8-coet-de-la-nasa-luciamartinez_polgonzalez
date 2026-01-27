package com.example.lazycomponents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazycomponents.api.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KaraokeViewModel : ViewModel() {

    private val repository = Repository()

    private val _lyrics = MutableLiveData<String>("Busca una canción...")
    val lyrics: LiveData<String> = _lyrics

    private val _coverUrl = MutableLiveData<String?>(null)
    val coverUrl: LiveData<String?> = _coverUrl

    private val _audioUrl = MutableLiveData<String?>(null)
    val audioUrl: LiveData<String?> = _audioUrl

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchLyrics(artist: String, title: String) {
        _isLoading.value = true
        _lyrics.value = "Buscando..."
        _coverUrl.value = null
        _audioUrl.value = null // Reseteamos el audio

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lyricsResponse = try {
                    repository.getLyrics(artist, title)
                } catch (e: Exception) { null }

                val musicData = try {
                    repository.getMusicData(artist, title)
                } catch (e: Exception) { null }

                withContext(Dispatchers.Main) {
                    _isLoading.value = false

                    _coverUrl.value = musicData?.coverUrl
                    _audioUrl.value = musicData?.audioUrl

                    if (lyricsResponse != null && lyricsResponse.isSuccessful) {
                        _lyrics.value = lyricsResponse.body()?.lyrics ?: "Letra no disponible"
                    } else {
                        _lyrics.value = "Error: Canción no encontrada"
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _lyrics.value = "Error de conexión: ${e.message}"
                }
            }
        }
    }
}