package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity

@Database(version = 1, entities = [FavoriteTrackEntity::class], exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteTrackDao():FavoriteTrackDao
}