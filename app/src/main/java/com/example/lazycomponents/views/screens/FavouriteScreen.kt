package com.example.lazycomponents.views.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lazycomponents.viewmodel.KaraokeViewModel

@Composable
fun FavoritesScreen(viewModel: KaraokeViewModel, onSongClick: () -> Unit) {
    val allFavorites by viewModel.getAllSongs().observeAsState(emptyList())
    val searchText by viewModel.searchText.observeAsState("")

    val filteredList = allFavorites.filter { song ->
        song.title.contains(searchText, ignoreCase = true) ||
                song.artist.contains(searchText, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.onSearchTextChange(it) },
            label = { Text("Filtrar mis favoritos...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredList) { song ->
                Card(elevation = CardDefaults.cardElevation(4.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable { viewModel.searchLyrics(song.artist, song.title)
                                       onSongClick()},
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (song.coverUrl != null) {
                            AsyncImage(model = song.coverUrl, contentDescription = null, modifier = Modifier.size(60.dp))
                        }
                        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                            Text(song.title, style = MaterialTheme.typography.titleMedium)
                            Text(song.artist, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        }
                        IconButton(onClick = { viewModel.deleteSong(song) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Borrar", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}