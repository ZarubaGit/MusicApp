package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {
    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(track: FavoriteTrackEntity)
    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun delete(track: FavoriteTrackEntity)
    @Query("SELECT * FROM favorite_entity_track ORDER BY insertTimeStamp DESC")
    suspend fun getAll(): List<FavoriteTrackEntity>?

    @Query("SELECT trackId FROM favorite_entity_track")
    suspend fun getId(): List<Int>?
}