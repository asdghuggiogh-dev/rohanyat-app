package com.rohaniyat.app.ui.screens.videoplayer

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.item
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.rohaniyat.app.data.model.formatViewsArabic
import com.rohaniyat.app.ui.components.VideoCard
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.EmeraldLight
import com.rohaniyat.app.ui.theme.Gold
import com.rohaniyat.app.ui.theme.GoldSoft
import com.rohaniyat.app.viewmodel.VideoViewModel
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerScreen(
    videoId: Long,
    onBack: () -> Unit,
    onOpenAnotherVideo: (Long) -> Unit,
    onOpenOptions: () -> Unit
) {
    val vm: VideoViewModel = viewModel()
    val context = LocalContext.current
    var video by remember { mutableStateOf<Video?>(null) }

    LaunchedEffect(videoId) { video = vm.findById(videoId) }

    val current = video ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    var controlsVisible by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(false) }
    var isMuted by remember { mutableStateOf(false) }
    var isFullscreen by remember { mutableStateOf(false) }
    var positionMs by remember { mutableLongStateOf(0L) }
    var durationMs by remember { mutableLongStateOf(0L) }
    var hasCountedView by remember(videoId) { mutableStateOf(false) }

    BackHandler(enabled = isFullscreen) { isFullscreen = false }

    val exoPlayer = remember(videoId) {
        ExoPlayer.Builder(context).build().apply {
            current.videoUri?.let { setMediaItem(MediaItem.fromUri(Uri.parse(it))) }
            prepare()
            playWhenReady = true // autoplay: this screen only opens from a direct user tap
        }
    }

    DisposableEffect(videoId) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
                if (playing && !hasCountedView) {
                    hasCountedView = true
                    vm.registerViewIfNeeded(current)
                    video = current.copy(viewCount = current.viewCount + 1)
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
            exoPlayer.release()
        }
    }

    // Smooth progress bar updates while playing
    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            positionMs = exoPlayer.currentPosition
            durationMs = exoPlayer.duration.coerceAtLeast(0)
            delay(300)
        }
    }
    // Auto-hide controls after a short delay while playing
    LaunchedEffect(controlsVisible, isPlaying) {
        if (controlsVisible && isPlaying) {
            delay(2500)
            controlsVisible = false
        }
    }

    Column(Modifier.fillMaxSize()) {
        // --- Player area ---
        Box(
            (if (isFullscreen) Modifier.fillMaxSize() else Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f))
                .background(Color.Black)
        ) {
            if (current.videoUri != null) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = false
                        }
                    },
                    modifier = Modifier.fillMaxSize().clickable { controlsVisible = !controlsVisible }
                )
            } else {
                Column(
                    Modifier.fillMaxSize().clickable { controlsVisible = !controlsVisible },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🎬", fontSize = 34.sp)
                    Spacer(Modifier.height(8.dp))
                    Text("محتوى تجريبي للعرض فقط", color = Color(0xFFCFD8D3), fontSize = 13.sp)
                    Text("لا يوجد ملف فيديو حقيقي مرفق به", color = Color(0xFFCFD8D3), fontSize = 13.sp)
                }
            }

            AnimatedVisibility(
                visible = controlsVisible,
                enter = fadeIn(), exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                PlayerControlsOverlay(
                    isPlaying = isPlaying,
                    isMuted = isMuted,
                    isFullscreen = isFullscreen,
                    positionMs = positionMs,
                    durationMs = durationMs,
                    hasRealMedia = current.videoUri != null,
                    onBack = onBack,
                    onOptions = onOpenOptions,
                    onTogglePlay = {
                        if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                        controlsVisible = true
                    },
                    onToggleMute = {
                        isMuted = !isMuted
                        exoPlayer.volume = if (isMuted) 0f else 1f
                    },
                    onToggleFullscreen = { isFullscreen = !isFullscreen },
                    onSeek = { fraction ->
                        val target = (durationMs * fraction).toLong()
                        exoPlayer.seekTo(target)
                        positionMs = target
                    }
                )
            }
        }

        // --- Info + related videos (hidden while fullscreen so the player takes the whole screen) ---
        if (!isFullscreen) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(current.title, fontWeight = FontWeight.Black, fontSize = 16.5.sp, modifier = Modifier.padding(18.dp, 14.dp, 18.dp, 6.dp))

                Row(Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    val avGradient = if (current.avatarColorIsGold) listOf(Gold, GoldSoft) else listOf(Emerald, EmeraldLight)
                    Box(Modifier.size(40.dp).clip(CircleShape).background(Brush.linearGradient(avGradient)), contentAlignment = Alignment.Center) {
                        Text(current.channelName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(current.channelName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            if (current.verified) {
                                Spacer(Modifier.width(4.dp))
                                Icon(Icons.Default.Verified, contentDescription = "موثّق", tint = Emerald, modifier = Modifier.size(13.dp))
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("👁️ ", fontSize = 12.sp)
                            Text(formatViewsArabic(current.viewCount), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                        }
                    }
                }

                var liked by remember { mutableStateOf(false) }
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ActionChip(if (liked) "❤️ إعجاب" else "🤍 إعجاب") { liked = !liked }
                    ActionChip("↗ مشاركة") {
                        val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, current.title) }
                        context.startActivity(Intent.createChooser(send, null))
                    }
                    ActionChip("🔖 حفظ") { }
                }

                if (current.description.isNotBlank()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(current.description, modifier = Modifier.padding(14.dp), fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f))
                    }
                }

                Text(
                    "فيديوهات مشابهة",
                    fontWeight = FontWeight.Black, fontSize = 14.5.sp,
                    modifier = Modifier.padding(18.dp, 16.dp, 18.dp, 6.dp)
                )
            }
            items(vm.videosExcluding(videoId), key = { it.id }) { related ->
                VideoCard(
                    video = related,
                    onClick = { onOpenAnotherVideo(related.id) },
                    onMoreClick = onOpenOptions
                )
            }
            item { Spacer(Modifier.height(40.dp)) }
        }
        }
    }
}

@Composable
private fun ActionChip(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) { Text(label, fontSize = 13.sp, fontWeight = FontWeight.Bold) }
}

@Composable
private fun PlayerControlsOverlay(
    isPlaying: Boolean,
    isMuted: Boolean,
    isFullscreen: Boolean,
    positionMs: Long,
    durationMs: Long,
    hasRealMedia: Boolean,
    onBack: () -> Unit,
    onOptions: () -> Unit,
    onTogglePlay: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleFullscreen: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.55f), Color.Transparent, Color.Transparent, Color.Black.copy(alpha = 0.6f))))
    ) {
        Row(Modifier.fillMaxWidth().padding(6.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBackIosNew, contentDescription = "رجوع", tint = Color.White) }
            IconButton(onClick = onOptions) { Icon(Icons.Default.MoreVert, contentDescription = "خيارات", tint = Color.White) }
        }
        Box(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            if (hasRealMedia) {
                IconButton(onClick = onTogglePlay, modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.Black.copy(alpha = 0.4f))) {
                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = "تشغيل/إيقاف", tint = Color.White)
                }
            }
        }
        if (hasRealMedia) {
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onTogglePlay, modifier = Modifier.size(32.dp)) {
                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                }
                Text(formatMillis(positionMs), color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 4.dp))
                Slider(
                    value = if (durationMs > 0) positionMs.toFloat() / durationMs else 0f,
                    onValueChange = { onSeek(it) },
                    modifier = Modifier.weight(1f).height(20.dp),
                    colors = SliderDefaults.colors(thumbColor = Color(0xFFC8A668), activeTrackColor = Color(0xFFC8A668))
                )
                Text(formatMillis(durationMs), color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = onToggleMute, modifier = Modifier.size(32.dp)) {
                    Icon(if (isMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp, contentDescription = null, tint = Color.White)
                }
                IconButton(onClick = onToggleFullscreen, modifier = Modifier.size(32.dp)) {
                    Icon(if (isFullscreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen, contentDescription = "ملء الشاشة", tint = Color.White)
                }
            }
        }
    }
}

private fun formatMillis(ms: Long): String {
    val totalSec = (ms / 1000).coerceAtLeast(0)
    val m = totalSec / 60
    val s = totalSec % 60
    return "%d:%02d".format(m, s)
}
