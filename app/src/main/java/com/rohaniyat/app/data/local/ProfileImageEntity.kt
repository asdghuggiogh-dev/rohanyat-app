package com.rohaniyat.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** kind is either "avatar" or "cover". Only one row exists per kind (kind is the primary key). */
@Entity(tableName = "profile_images")
data class ProfileImageEntity(
    @PrimaryKey val kind: String,
    val filePath: String
)
