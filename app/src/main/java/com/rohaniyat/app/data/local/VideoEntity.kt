package com.rohaniyat.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Persisted record for a video/short the user has published from this device.
 * [filePath] points at a copy of the media file this app made in its own internal
 * storage (see MediaStorage.kt) — this is what makes uploads survive app restarts,
 * unlike the web prototype which could only hold a Blob in memory/IndexedDB.
 */
@Entity(tableName = "uploaded_videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val hashtags: String,
    val filePath: String,
    val durationLabel: String,
    val isShort: Boolean,
    val viewCount: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)
