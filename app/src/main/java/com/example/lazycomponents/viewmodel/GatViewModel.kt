package com.example.lazycomponents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lazycomponents.model.Gat

class GatViewModel : ViewModel() {
    private val _llistaGats = MutableLiveData<List<Gat>>()
    val llistaGats: LiveData<List<Gat>> = _llistaGats

    init {
        cargarGatos()
    }

    /*
    data class LetrasRespuesta(
        val movies: List<Letras>
    )

    data class Letras(
        val artist: String,
        val title: String
    )
     */
    
    private fun cargarGatos() {
        _llistaGats.value = listOf(
            Gat("1", "Michi Táctico", listOf("serio", "militar"), "https://cataas.com/cat?v=1", "Listo para la batalla."),
            Gat("2", "Sr. Bigotes", listOf("blanco", "elegante"), "https://cataas.com/cat?v=2", "Un gato con clase."),
            Gat("3", "Garfield", listOf("naranja", "comilón"), "https://cataas.com/cat?v=3", "Ama la lasaña."),
            Gat("4", "Sombra", listOf("negro", "ninja"), "https://cataas.com/cat?v=4", "Se esconde en la oscuridad."),
            Gat("5", "Nube", listOf("blanco", "suave"), "https://cataas.com/cat?v=5", "Parece algodón."),
            Gat("6", "Manchas", listOf("bicolor", "juguetón"), "https://cataas.com/cat?v=6", "Nunca para quieto."),
            Gat("7", "Dormilón", listOf("perezoso"), "https://cataas.com/cat?v=7", "Zzzzz..."),
            Gat("8", "Hacker", listOf("tech", "geek"), "https://cataas.com/cat?v=8", "Arreglando bugs.")
        )
    }

    fun obtenerGato(id: String): Gat? {
        return _llistaGats.value?.find { it.id == id }
    }
}