package com.example.lazycomponents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.lazycomponents.ui.theme.LazyComponentsTheme
import com.example.lazycomponents.view.KaraokeScreen
import com.example.lazycomponents.viewmodel.KaraokeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: KaraokeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyComponentsTheme {
            KaraokeScreen(viewModel = viewModel)
            }
        }
    }
}