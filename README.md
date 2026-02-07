# KARAOKE APP | COHETE A LA NASA

## Descripción del Proyecto
Aplicación Android desarrollada con Jetpack Compose que permite buscar letras de canciones, escuchar previsualizaciones de audio, ver carátulas de álbumes y guardar canciones favoritas localmente. Siguiendo el patrón MVVM con LiveData.

## Estructura del Proyecto

```
com.example.lazycomponents/
├── api/
│   ├── LyricsApi.kt         # API de letras (lrclib.net)
│   ├── ItunesApi.kt         # API de música (iTunes)
│   └── Repository.kt        # Repositorio central
├── local/
│   ├── KaraokeDatabase.kt   # Base de datos Room
│   └── SongsDAO.kt          # Operaciones CRUD
├── model/
│   ├── ItunesModel.kt       # Modelo iTunes
│   └── LyricsModel.kt       # Modelo letras y song
├── nav/
│   └── AppNavigation.kt     # Navegación
├── viewmodel/
│   └── KaraokeViewModel.kt  # Lógica del programa
├── views/screens/
│   ├── HomeScreen.kt        # Búsqueda y top 10
│   └── FavoritesScreen.kt   # Gestión de favoritos
└── MainActivity.kt          # Punto de entrada
```

## APIs Utilizadas

### 1. **LRCLib API** (Letras)
- **URL Base**: `https://lrclib.net/`
- **Endpoint**: `/api/get`
- **Parámetros**: `artist_name`, `track_name`

### 2. **iTunes API** (Música)
- **URL Base**: `https://itunes.apple.com/`
- **Endpoint**: `/search`
- **Parámetros**: `term`, `media=music`, `limit`, `entity=song`

## Características Principales

### Búsqueda de Canciones
- Búsqueda por artista y título
- Integración con dos APIs simultáneamente

### Reproductor de Audio
- Previsualización de 30 segundos desde iTunes
- Controles play/pause integrados

### Sistema de Favoritos
- Guardado offline con Room
- Sincronización automática entre pantallas
- Búsqueda local en favoritos

### Interfaz de Usuario
- Navegación por pestañas (Home/Favoritos)
- Diseño responsiv
- Top 10 de canciones populares

## Capturas de Pantalla
| Pantalla Principal | Resulatdo de la búsqueda | Pantalla de Canciones Favoritas |
|-------------------|-----------------|-----------------|
| ![HomeScreen](screenshots/HomeScreen.png) | ![SearchResult](screenshots/SearchResult.png) |![FavoriteSongs](screenshots/FavoriteSongs.png) |


## Base de Datos Local
### Entidad Song
```kotlin
@Entity(tableName = "songs_table")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val artist: String,
    val title: String,
    val lyrics: String,
    val coverUrl: String? = null,
    val audioUrl: String? = null,
    val isFavorite: Boolean = false
)
```

### Operaciones Disponibles
- `getAllSongs()` - Lista todos los favoritos
- `getSongByArtistAndTitle()` - Búsqueda específica
- `insertSong()` - Añadir nuevo favorito
- `deleteSong()` - Eliminar favorito

## Autores
Lucía Martínez <br>
Pol González <br>
Módulo 0488: Desarollo de interfaces

## Licencia
Proyecto educativo desarrollado para fines académicos. Las APIs utilizadas son de acceso público.
