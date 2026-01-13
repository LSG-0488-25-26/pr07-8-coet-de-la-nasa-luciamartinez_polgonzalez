package com.example.lazycomponents.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lazycomponents.model.Gat

class GatViewModel : ViewModel() {
    private val _llistaGats = MutableLiveData<List<Gat>>()
    val llistaGats: LiveData<List<Gat>> = _llistaGats
}