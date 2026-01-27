package com.example.lazycomponents.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lazycomponents.api.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KaraokeViewModel : ViewModel() {
    private val repository = Repository()

    private val _lyrics = MutableLiveData<String>("Busca una canción para ver la letra...")
    val lyrics: LiveData<String> = _lyrics

    private val _coverUrl = MutableLiveData<String?>(null)
    val coverUrl: LiveData<String?> = _coverUrl

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchLyrics(artist: String, title: String) {
        _isLoading.value = true
        _lyrics.value = "Buscando..."
        _coverUrl.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val lyricsResponse = try {
                    repository.getLyrics(artist, title)
                } catch (e: Exception) {
                    null
                }

                val coverResult = try {
                    repository.getCover(artist, title)
                } catch (e: Exception) {
                    null
                }

                withContext(Dispatchers.Main) {
                    _isLoading.value = false

                    _coverUrl.value = coverResult

                    if (lyricsResponse != null && lyricsResponse.isSuccessful) {
                        _lyrics.value = lyricsResponse.body()?.lyrics ?: "Letra vacía"
                    } else {
                        _lyrics.value = if (lyricsResponse == null) {
                            "Error: El servidor tardó demasiado (Timeout). Inténtalo de nuevo."
                        } else {
                            "Error: Canción no encontrada (404)"
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _lyrics.value = "Error desconocido: ${e.message}"
                }
            }
        }
    }
}