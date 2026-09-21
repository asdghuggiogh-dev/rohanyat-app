package com.rohaniyat.app

import android.app.Application
import com.rohaniyat.app.data.local.AppDatabase
import com.rohaniyat.app.data.repository.ProfileRepository
import com.rohaniyat.app.data.repository.VideoRepository

class RohaniyatApplication : Application() {

    lateinit var videoRepository: VideoRepository
        private set
    lateinit var profileRepository: ProfileRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        videoRepository = VideoRepository(db.videoDao())
        profileRepository = ProfileRepository(db.profileImageDao())
    }
}
