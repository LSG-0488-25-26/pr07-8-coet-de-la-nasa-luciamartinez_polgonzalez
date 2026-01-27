package com.example.lazycomponents.viewmodel

import android.util.Log
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

    private val _lyrics = MutableLiveData<String>("Busca una canción para ver la letra...")
    val lyrics: LiveData<String> = _lyrics

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchLyrics(artist: String, title: String) {
        _isLoading.value = true
        _lyrics.value = "Buscando..."

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getLyrics(artist, title)

                withContext(Dispatchers.Main) {
                    _isLoading.value = false

                    if (response.isSuccessful) {
                        _lyrics.value = response.body()?.lyrics ?: "Letra vacía"
                    } else {
                        _lyrics.value = "Error: Canción no encontrada (404)"
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _lyrics.value = "Fallo de conexión: ${e.message}"
                    Log.e("KaraokeVM", "Excepción capturada", e)
                }
            }
        }
    }
}