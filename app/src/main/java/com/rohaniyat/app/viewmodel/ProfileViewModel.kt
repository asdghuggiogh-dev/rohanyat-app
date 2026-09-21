package com.rohaniyat.app.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rohaniyat.app.RohaniyatApplication
import com.rohaniyat.app.data.local.MediaStorage
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = (application as RohaniyatApplication).profileRepository

    var avatarPath by mutableStateOf<String?>(null)
        private set
    var coverPath by mutableStateOf<String?>(null)
        private set
    var bioExpanded by mutableStateOf(false)

    init {
        viewModelScope.launch {
            avatarPath = repo.getAvatarPath()
            coverPath = repo.getCoverPath()
        }
    }

    fun setAvatar(uri: Uri) {
        viewModelScope.launch {
            val path = MediaStorage.copyImageToInternalStorage(getApplication(), uri)
            repo.setAvatarPath(path)
            avatarPath = path
        }
    }

    fun setCover(uri: Uri) {
        viewModelScope.launch {
            val path = MediaStorage.copyImageToInternalStorage(getApplication(), uri)
            repo.setCoverPath(path)
            coverPath = path
        }
    }
}
