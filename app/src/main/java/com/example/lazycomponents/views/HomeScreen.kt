package com.example.lazycomponents.views

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lazycomponents.model.ItunesResult
import com.example.lazycomponents.utils.ScreenOrientation
import com.example.lazycomponents.utils.getAdaptativeFontSize
import com.example.lazycomponents.utils.getAdaptativePadding
import com.example.lazycomponents.utils.getScreenInfo
import com.example.lazycomponents.utils.getScreenSize
import com.example.lazycomponents.viewmodel.KaraokeViewModel

@Composable
fun HomeScreen(
    viewModel: KaraokeViewModel,
    screenPadding: Dp = getAdaptativePadding(getScreenSize())
) {
    val lyrics by viewModel.lyrics.observeAsState("")
    val topSongs by viewModel.topSongs.observeAsState(emptyList())
    val coverUrl by viewModel.coverUrl.observeAsState(null)
    val audioUrl by viewModel.audioUrl.observeAsState(null)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isFavorite by viewModel.isFavorite.observeAsState(false)
    val showPlayer =
        !lyrics.startsWith("Busca") && !lyrics.startsWith("Error") && !lyrics.startsWith("No se encontró")

    var artist by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val screenInfo = getScreenInfo()
    val isLandscape = screenInfo.orientation == ScreenOrientation.LANDSCAPE

    if (isLandscape && !showPlayer) {
        // Horizontal
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            horizontalArrangement = Arrangement.spacedBy(screenPadding)
        ) {
            // Buscar 40%
            Column(
                modifier = Modifier.weight(0.4f),
                verticalArrangement = Arrangement.spacedBy(screenPadding / 3)
            ) {
                Text(
                    text = "Buscar",
                    fontSize = getAdaptativeFontSize(getScreenSize(), 22).sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = screenPadding / 4)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(screenPadding / 4)
                ) {
                    OutlinedTextField(
                        value = artist,
                        onValueChange = { artist = it },
                        label = { Text("Artista") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(screenPadding / 2))

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Canción") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(screenPadding))

                    // Botones
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(screenPadding / 2)
                    ) {
                        Button(
                            onClick = { viewModel.searchLyrics(artist, title) },
                            enabled = !isLoading && artist.isNotBlank() && title.isNotBlank(),
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Buscar")
                            }
                        }

                        Button(
                            onClick = { viewModel.toggleFavorite() },
                            enabled = !isLoading && lyrics.isNotEmpty() && !lyrics.startsWith("Busca"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Favorito",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
            // Top 10 60%
            Column(
                modifier = Modifier.weight(0.6f)
            ) {
                Text(
                    text = "Top 10 hits en iTunes",
                    fontSize = getAdaptativeFontSize(getScreenSize(), 22).sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = screenPadding / 4)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(screenPadding / 2)
                ) {
                    items(topSongs) { song ->
                        TopSongItem(
                            song = song,
                            onClick = {
                                artist = song.artistName ?: ""
                                title = song.trackName ?: ""
                                viewModel.searchLyrics(artist, title)
                            },
                            isCompact = false
                        )
                    }
                }
            }
        }
    } else if (isLandscape && showPlayer) {
        // Horizontal
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding)
        ) {
            // Columna izquierda: Búsqueda y Top 10
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(screenPadding)
            ) {
                Text(
                    text = "Karaoke App",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = screenPadding / 2)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(screenPadding / 2)
                ) {
                    OutlinedTextField(
                        value = artist,
                        onValueChange = { artist = it },
                        label = { Text("Artista") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Canción") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(screenPadding / 2)
                ) {
                    Button(
                        onClick = { viewModel.searchLyrics(artist, title) },
                        enabled = !isLoading && artist.isNotBlank() && title.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Buscar", fontSize = 14.sp)
                        }
                    }

                    Button(
                        onClick = { viewModel.toggleFavorite() },
                        enabled = !isLoading && lyrics.isNotEmpty() && !lyrics.startsWith("Busca"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(screenPadding))
                Divider()
                Spacer(modifier = Modifier.height(screenPadding / 2))

                // Top 10
                Text(
                    text = "Top 10 Hits",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = getAdaptativeFontSize(getScreenSize(), 18).sp,
                    modifier = Modifier.padding(bottom = screenPadding / 2)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(screenPadding / 4)
                ) {
                    items(topSongs) { song ->
                        TopSongItem(
                            song = song,
                            onClick = {
                                artist = song.artistName ?: ""
                                title = song.trackName ?: ""
                                viewModel.searchLyrics(artist, title)
                            },
                            isCompact = true
                        )
                    }
                }
            }

            // Separador vertical
            Spacer(modifier = Modifier.width(screenPadding))
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            Spacer(modifier = Modifier.width(screenPadding))

            // Columna derecha: Resultados
            Column(
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(screenPadding)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenPadding),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (coverUrl != null) {
                        AsyncImage(
                            model = coverUrl,
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(screenPadding / 2)
                    ) {
                        Text(
                            text = title.ifBlank { "Canción" },
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = getAdaptativeFontSize(getScreenSize(), 20).sp
                        )
                        Text(
                            text = artist.ifBlank { "Artista" },
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = getAdaptativeFontSize(getScreenSize(), 16).sp
                        )

                        if (audioUrl != null) {
                            SimpleAudioPlayer(url = audioUrl!!)
                        }
                    }
                }

                Divider()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = lyrics,
                        textAlign = TextAlign.Center,
                        fontSize = getAdaptativeFontSize(getScreenSize(), 16).sp,
                        lineHeight = (getAdaptativeFontSize(getScreenSize(), 16) * 1.5).sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    } else {
        // Vertical
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Karaoke App",
                fontSize = getAdaptativeFontSize(getScreenSize(), 28).sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(screenPadding))

            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                label = { Text("Artista") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(screenPadding / 2))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Canción") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(screenPadding))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenPadding / 2)
            ) {
                Button(
                    onClick = { viewModel.searchLyrics(artist, title) },
                    enabled = !isLoading && artist.isNotBlank() && title.isNotBlank(),
                    modifier = Modifier.weight(1f)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Buscar")
                    }
                }

                Button(
                    onClick = { viewModel.toggleFavorite() },
                    enabled = !isLoading && lyrics.isNotEmpty() && !lyrics.startsWith("Busca"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenPadding))
            Divider()
            Spacer(modifier = Modifier.height(screenPadding))

            if (showPlayer) {
                if (coverUrl != null || audioUrl != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                            .padding(screenPadding)
                    ) {
                        if (coverUrl != null) {
                            AsyncImage(
                                model = coverUrl,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp)
                            )
                        }

                        if (audioUrl != null) {
                            Spacer(modifier = Modifier.width(screenPadding))
                            SimpleAudioPlayer(url = audioUrl!!)
                        }
                    }
                    Spacer(modifier = Modifier.height(screenPadding))
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = lyrics,
                        textAlign = TextAlign.Center,
                        fontSize = getAdaptativeFontSize(getScreenSize(), 18).sp,
                        lineHeight = (getAdaptativeFontSize(getScreenSize(), 18) * 1.5).sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Text(
                    text = "Top 10 hits en iTunes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = getAdaptativeFontSize(getScreenSize(), 20).sp
                )
                Spacer(modifier = Modifier.height(screenPadding / 2))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(screenPadding / 2)
                ) {
                    items(topSongs) { song ->
                        TopSongItem(
                            song = song,
                            onClick = {
                                artist = song.artistName ?: ""
                                title = song.trackName ?: ""
                                viewModel.searchLyrics(artist, title)
                            },
                            isCompact = false
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopSongItem(
    song: ItunesResult,
    onClick: () -> Unit,
    isCompact: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = if (!isCompact) 4.dp else 0.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = if (isCompact) 8.dp else 12.dp,
                vertical = if (isCompact) 8.dp else 12.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = song.artworkUrl,
                contentDescription = null,
                modifier = Modifier.size(if (isCompact) 40.dp else 50.dp)
            )

            Spacer(modifier = Modifier.width(if (isCompact) 12.dp else 16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.trackName ?: "Desconocido",
                    style = if (isCompact) MaterialTheme.typography.bodyMedium
                    else MaterialTheme.typography.titleMedium,
                    fontWeight = if (isCompact) FontWeight.Medium else FontWeight.Bold,
                    maxLines = 1,  // Siempre 1 línea para ambos
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = song.artistName ?: "Desconocido",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// SimpleAudioPlayer
@Composable
fun SimpleAudioPlayer(url: String) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }

    val mediaPlayer = remember {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try {
                if (mediaPlayer.isPlaying) mediaPlayer.stop()
                mediaPlayer.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(url) {
        try {
            if (isPlaying) {
                mediaPlayer.stop(); isPlaying = false
            }
            mediaPlayer.reset()
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnCompletionListener { isPlaying = false }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
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