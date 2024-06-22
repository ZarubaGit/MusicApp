package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.data.db.entity.PlayListEntity
@Dao
interface PlayListDao {

    @Insert(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(playList: PlayListEntity)

    @Update(entity = PlayListEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(playList: PlayListEntity)

    @Query("SELECT * FROM playlist_entity ORDER BY id ASC")
    suspend fun getAll(): List<PlayListEntity>?
}