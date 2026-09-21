package com.rohaniyat.app.data.local

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileImageDao {
    @Query("SELECT * FROM profile_images WHERE kind = :kind LIMIT 1")
    suspend fun get(kind: String): ProfileImageEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ProfileImageEntity)
}
