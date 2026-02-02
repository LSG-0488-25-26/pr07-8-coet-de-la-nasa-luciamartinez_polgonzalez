package com.example.lazycomponents.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
fun HomeScreen(viewModel: KaraokeViewModel) {
    val lyrics by viewModel.lyrics.observeAsState("")
    val topSongs by viewModel.topSongs.observeAsState(emptyList())
    val isSearching = !lyrics.startsWith("Busca") && !lyrics.startsWith("Error")
    val coverUrl by viewModel.coverUrl.observeAsState(null)
    val audioUrl by viewModel.audioUrl.observeAsState(null)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isFavorite by viewModel.isFavorite.observeAsState(false)
    val showPlayer = !lyrics.startsWith("Busca") && !lyrics.startsWith("Error") && !lyrics.startsWith("No se encontró")

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.searchLyrics(artist, title) },
                enabled = !isLoading && artist.isNotBlank() && title.isNotBlank(),
                modifier = Modifier.weight(1f)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Buscar")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.toggleFavorite() },
                enabled = !isLoading && lyrics.isNotEmpty() && !lyrics.startsWith("Busca"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) Color(0xFFE91E63) else Color.Gray
                )
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        if (showPlayer) {
            if (coverUrl != null || audioUrl != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
                        .padding(10.dp)
                ) {
                    if (coverUrl != null) {
                        AsyncImage(
                            model = coverUrl,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp)
                        )
                    }

                    if (audioUrl != null) {
                        Spacer(modifier = Modifier.width(16.dp))
                        SimpleAudioPlayer(url = audioUrl!!)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Box(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                Text(text = lyrics, textAlign = TextAlign.Center, fontSize = 18.sp, lineHeight = 28.sp, modifier = Modifier.fillMaxWidth())
            }
        } else {
            Text("Top 10 hits en ITunes", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn (verticalArrangement = Arrangement.spacedBy(8.dp)){
                items(topSongs) { song ->
                    TopSongItem(song) { 
                        artist = song.artistName ?: ""
                        title = song.trackName ?: ""
                        viewModel.searchLyrics(artist, title)
                    }
                }
            }
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
            if (isPlaying) { mediaPlayer.stop(); isPlaying = false }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnCompletionListener { isPlaying = false }
        } catch (e: Exception) { e.printStackTrace() }
    }

    IconButton(
        onClick = {
            try {
                if (isPlaying) {
                    mediaPlayer.pause()
                } else {
                    mediaPlayer.start()
                }
                isPlaying = !isPlaying
            } catch (e: Exception) { e.printStackTrace() }
        },
        modifier = Modifier
            .size(50.dp)
            .background(MaterialTheme.colorScheme.primary, CircleShape)
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = "Play",
            tint = Color.White
        )
    }
}

@Composable
fun TopSongItem(song: com.example.lazycomponents.model.ItunesResult, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable{ onClick() },
        elevation = CardDefaults.cardElevation(2.dp)){
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(model = song.artworkUrl, contentDescription = null, modifier = Modifier.size(50.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(song.trackName ?: "Desconocido", fontWeight = FontWeight.Bold)
                Text(song.artistName ?: "Desconocido", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}