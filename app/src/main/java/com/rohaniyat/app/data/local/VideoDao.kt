package com.rohaniyat.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoDao {
    @Query("SELECT * FROM uploaded_videos ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<VideoEntity>>

    @Query("SELECT * FROM uploaded_videos ORDER BY createdAt DESC")
    suspend fun getAll(): List<VideoEntity>

    @Insert
    suspend fun insert(video: VideoEntity): Long

    @Update
    suspend fun update(video: VideoEntity)

    @Query("UPDATE uploaded_videos SET viewCount = viewCount + 1 WHERE id = :id")
    suspend fun incrementViews(id: Long)

    @Query("DELETE FROM uploaded_videos WHERE id = :id")
    suspend fun delete(id: Long)
}
