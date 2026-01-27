package com.example.lazycomponents.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lazycomponents.model.Song

@Database(entities = [Song::class], version = 1, exportSchema = false)
abstract class KaraokeDatabase : RoomDatabase() {

    abstract fun songsDao(): SongsDao

    companion object {
        @Volatile
        private var INSTANCE: KaraokeDatabase? = null

        fun getDatabase(context: Context): KaraokeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KaraokeDatabase::class.java,
                    "karaoke_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}