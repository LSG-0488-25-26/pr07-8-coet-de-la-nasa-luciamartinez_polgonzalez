package com.example.lazycomponents.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.lazycomponents.model.Gat
import com.example.lazycomponents.viewmodel.GatViewModel

@Composable
fun ItemGat(gat: Gat, alHacerClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { alHacerClick(gat.id) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = gat.urlImagen,
                contentDescription = null,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = gat.nombre, fontSize = 20.sp)
                Text(text = gat.tags.joinToString(", "), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun PantallaLlistaGats(viewModel: GatViewModel, alNavegarDetalle: (String) -> Unit) {
    val gats by viewModel.llistaGats.observeAsState(initial = emptyList())

    Scaffold(
        topBar = { Text("Lista de Gatos", modifier = Modifier.padding(16.dp), fontSize = 24.sp) }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(gats) { gat ->
                ItemGat(gat = gat, alHacerClick = alNavegarDetalle)
            }
        }
    }
}

@Composable
fun PantallaDetallsGat(viewModel: GatViewModel, gatoId: String, alVolver: () -> Unit) {
    val gat = viewModel.obtenerGato(gatoId)

    if (gat != null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Button(onClick = alVolver) { Text("Volver") }
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = gat.urlImagen,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = gat.nombre, fontSize = 30.sp)
            Text(text = gat.descripcion, fontSize = 18.sp)
        }
    } else {
        Text("Gato no encontrado", modifier = Modifier.padding(16.dp))
    }
}