package com.example.lazycomponents.views

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lazycomponents.utils.getAdaptativeFontSize
import com.example.lazycomponents.utils.getAdaptativePadding
import com.example.lazycomponents.utils.getScreenSize
import com.example.lazycomponents.viewmodel.KaraokeViewModel

@Composable
fun FavoritesScreen(
    viewModel: KaraokeViewModel,
    onSongClick: () -> Unit,
    screenPadding: Dp = getAdaptativePadding(getScreenSize())
) {
    val allFavorites by viewModel.getAllSongs().observeAsState(emptyList())
    val searchText by viewModel.searchText.observeAsState("")

    val filteredList = allFavorites.filter { song ->
        song.title.contains(searchText, ignoreCase = true) ||
                song.artist.contains(searchText, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenPadding)
    ) {
        Text(
            text = "⭐ Mis Favoritos",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            fontSize = getAdaptativeFontSize(getScreenSize(), 24).sp,
            modifier = Modifier.padding(bottom = screenPadding)
        )

        OutlinedTextField(
            value = searchText,
            onValueChange = { viewModel.onSearchTextChange(it) },
            label = { Text("Filtrar mis favoritos...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(screenPadding))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(screenPadding / 2)
        ) {
            items(filteredList) { song ->
                Card(
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(screenPadding / 2)
                            .clickable {
                                viewModel.searchLyrics(song.artist, song.title)
                                onSongClick()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (song.coverUrl != null) {
                            AsyncImage(
                                model = song.coverUrl,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = screenPadding / 2)
                        ) {
                            Text(
                                text = song.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = getAdaptativeFontSize(getScreenSize(), 16).sp
                            )
                            Text(
                                text = song.artist,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray,
                                fontSize = getAdaptativeFontSize(getScreenSize(), 14).sp
                            )
                        }
                        IconButton(
                            onClick = { viewModel.deleteSong(song) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Borrar",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}