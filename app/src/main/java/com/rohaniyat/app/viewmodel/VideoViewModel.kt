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
import com.rohaniyat.app.data.model.Video
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VideoViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as RohaniyatApplication).videoRepository

    val videos: StateFlow<List<Video>> = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Per-session guard so a single viewing doesn't count more than one view, and so simply
    // opening the player without pressing play never counts a view at all.
    private val countedThisSession = mutableSetOf<Long>()

    var publishState by mutableStateOf<PublishState>(PublishState.Idle)
        private set

    sealed class PublishState {
        data object Idle : PublishState()
        data object Saving : PublishState()
        data class Done(val isShort: Boolean) : PublishState()
        data class Error(val message: String) : PublishState()
    }

    fun resetPublishState() { publishState = PublishState.Idle }

    fun publish(
        sourceUri: Uri,
        title: String,
        description: String,
        hashtags: String,
        durationLabel: String,
        isShort: Boolean
    ) {
        viewModelScope.launch {
            publishState = PublishState.Saving
            try {
                val path = MediaStorage.copyVideoToInternalStorage(getApplication(), sourceUri)
                repo.publish(
                    title = title.ifBlank { "بدون عنوان" },
                    description = description,
                    hashtags = hashtags.ifBlank { "#محتوى_جديد" },
                    filePath = path,
                    durationLabel = durationLabel,
                    isShort = isShort
                )
                publishState = PublishState.Done(isShort)
            } catch (e: Exception) {
                publishState = PublishState.Error(e.message ?: "فشل النشر، حاول مرة أخرى")
            }
        }
    }

    suspend fun findById(id: Long): Video? = repo.findById(id)

    /** Call this exactly when real playback starts (the player's onIsPlaying callback), not on open. */
    fun registerViewIfNeeded(video: Video) {
        if (countedThisSession.contains(video.id)) return
        countedThisSession.add(video.id)
        viewModelScope.launch { repo.registerView(video) }
    }

    fun videosExcluding(id: Long): List<Video> = videos.value.filter { it.id != id }

    fun searchAll(query: String): List<Video> {
        if (query.isBlank()) return emptyList()
        return videos.value.filter {
            it.title.contains(query, ignoreCase = true) || it.channelName.contains(query, ignoreCase = true)
        }
    }
}
