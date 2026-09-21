package com.rohaniyat.app.ui.screens.shorts

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.rohaniyat.app.data.model.Video
import com.rohaniyat.app.viewmodel.VideoViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortsScreen() {
    val vm: VideoViewModel = viewModel()
    val allVideos by vm.videos.collectAsState()
    val displayShorts = remember(allVideos) { allVideos.filter { it.isShort } }

    if (displayShorts.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("▶️", fontSize = 40.sp)
                Spacer(Modifier.height(8.dp))
                Text("لا توجد Shorts بعد", fontWeight = FontWeight.Bold)
                Text("انشر أول Short من زر \"إنشاء\"", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { displayShorts.size })
    VerticalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        ShortSlide(video = displayShorts[page], isActive = pagerState.currentPage == page, viewModel = vm)
    }
}

@Composable
private fun ShortSlide(video: Video, isActive: Boolean, viewModel: VideoViewModel) {
    val context = LocalContext.current
    var isMuted by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(true) }
    var liked by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }
    var following by remember { mutableStateOf(false) }

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            video.videoUri?.let { setMediaItem(MediaItem.fromUri(Uri.parse(it))) }
            repeatMode = Player.REPEAT_MODE_ONE
            volume = 0f
            prepare()
        }
    }

    LaunchedEffect(isActive) {
        if (isActive) {
            exoPlayer.play()
            viewModel.registerViewIfNeeded(video)
        } else {
            exoPlayer.pause()
        }
    }
    LaunchedEffect(isMuted) { exoPlayer.volume = if (isMuted) 0f else 1f }

    DisposableEffect(Unit) { onDispose { exoPlayer.release() } }

    Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF158A68), Color(0xFF094A39))))) {
        if (video.videoUri != null) {
            AndroidView(
                factory = {
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = false
                        setOnClickListener {
                            isPlaying = !isPlaying
                            if (isPlaying) exoPlayer.play() else exoPlayer.pause()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.15f), Color.Transparent, Color.Black.copy(alpha = 0.6f)))))

        Row(
            Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(34.dp).clip(CircleShape).background(Color(0xFFC8A668)))
                    Spacer(Modifier.width(8.dp))
                    Text(video.channelName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = { following = !following }, modifier = Modifier.height(26.dp)) {
                        Text(if (following) "متابَع" else "متابعة", fontSize = 10.sp, color = Color.White)
                    }
                }
                Spacer(Modifier.height(8.dp))
                Text(video.title, color = Color.White, fontSize = 13.sp)
                Text(video.hashtags, color = Color(0xFFE6D5AB), fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(18.dp)) {
                ShortAction(if (liked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, "${video.viewCount}") { liked = !liked }
                ShortAction(Icons.Default.Comment, "عرض") { }
                ShortAction(Icons.Default.Repeat, "نشر") { }
                ShortAction(if (saved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, "حفظ") { saved = !saved }
                ShortAction(Icons.Default.Share, "مشاركة") { }
                if (video.videoUri != null) {
                    ShortAction(if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp, "صوت") { isMuted = !isMuted }
                }
            }
        }
    }
}

@Composable
private fun ShortAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable(onClick = onClick)) {
        Icon(icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(26.dp))
        Text(label, color = Color.White, fontSize = 11.sp)
    }
}
