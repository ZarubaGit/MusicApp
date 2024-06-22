package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.data.db.dao.PlayListDao
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity
import com.example.playlistmaker.data.db.entity.PlayListEntity

@Database(version = 2, entities = [FavoriteTrackEntity::class, PlayListEntity::class], exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteTrackDao():FavoriteTrackDao
    abstract fun getPlayerListDao():PlayListDao
}