package com.example.lazycomponents.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lazycomponents.viewmodel.KaraokeViewModel

@Composable
fun KaraokeScreen(viewModel: KaraokeViewModel) {
    val lyrics by viewModel.lyrics.observeAsState("")
    val coverUrl by viewModel.coverUrl.observeAsState(null)
    val audioUrl by viewModel.audioUrl.observeAsState(null)
    val isLoading by viewModel.isLoading.observeAsState(false)

    var artist by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Karaoke App",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Entradas de texto
        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("Artista") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Canción") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.searchLyrics(artist, title) },
            enabled = !isLoading && artist.isNotBlank() && title.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLoading) "Buscando..." else "Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Foto
            if (coverUrl != null) {
                AsyncImage(
                    model = coverUrl,
                    contentDescription = "Carátula",
                    modifier = Modifier.size(100.dp)
                )
            }

            if (audioUrl != null) {
                Spacer(modifier = Modifier.width(16.dp))
                SimpleAudioPlayer(url = audioUrl!!) // Llamada a la función de abajo
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Box(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
            Text(
                text = lyrics,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SimpleAudioPlayer(url: String) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    val mediaPlayer = remember {
        android.media.MediaPlayer().apply {
            setAudioAttributes(
                android.media.AudioAttributes.Builder()
                    .setContentType(android.media.AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(android.media.AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) { e.printStackTrace() }
        }
    }

    LaunchedEffect(url) {
        try {
            if (isPlaying) {
                mediaPlayer.stop()
                isPlaying = false
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnCompletionListener { isPlaying = false }
        } catch (e: Exception) { e.printStackTrace() }
    }

    IconButton(
        onClick = {
            if (isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
            isPlaying = !isPlaying
        },
        modifier = Modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Play/Pause",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}