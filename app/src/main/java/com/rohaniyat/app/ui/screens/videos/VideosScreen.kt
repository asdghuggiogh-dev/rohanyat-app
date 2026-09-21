package com.rohaniyat.app.ui.screens.videos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohaniyat.app.ui.components.VideoCard
import com.rohaniyat.app.viewmodel.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideosScreen(onOpenVideo: (Long) -> Unit, onOpenSearch: () -> Unit, onOpenVideoOptions: () -> Unit) {
    val vm: VideoViewModel = viewModel()
    val videos by vm.videos.collectAsState()

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("الفيديوهات") },
            actions = { IconButton(onClick = onOpenSearch) { Icon(Icons.Default.Search, contentDescription = "بحث") } }
        )
        LazyColumn(contentPadding = PaddingValues(horizontal = 0.dp)) {
            items(videos, key = { it.id }) { video ->
                VideoCard(
                    video = video,
                    onClick = { onOpenVideo(video.id) },
                    onMoreClick = onOpenVideoOptions,
                    modifier = Modifier.padding(horizontal = 0.dp)
                )
            }
        }
    }
}
