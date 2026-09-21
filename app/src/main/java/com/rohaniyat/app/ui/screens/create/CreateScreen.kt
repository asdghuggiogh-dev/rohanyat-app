package com.rohaniyat.app.ui.screens.create

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.viewmodel.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(onPublished: (isShort: Boolean) -> Unit) {
    val vm: VideoViewModel = viewModel()
    val context = LocalContext.current

    var pickedUri by remember { mutableStateOf<Uri?>(null) }
    var isShort by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var hashtags by remember { mutableStateOf("") }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) pickedUri = uri
    }

    LaunchedEffect(vm.publishState) {
        val state = vm.publishState
        if (state is VideoViewModel.PublishState.Done) {
            pickedUri = null; title = ""; description = ""; hashtags = ""
            onPublished(state.isShort)
            vm.resetPublishState()
        }
    }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("إنشاء محتوى") })
        Column(Modifier.fillMaxSize().padding(18.dp)) {
            if (pickedUri == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .border(2.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                        .clickable {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⬆️", fontSize = 30.sp)
                        Spacer(Modifier.height(6.dp))
                        Text("رفع فيديو أو Short", fontWeight = FontWeight.Bold)
                        Text("اختر من ألبوماتك أو مقاطع الفيديو المحفوظة في جهازك", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            } else {
                Box(Modifier.fillMaxWidth().height(200.dp).background(Color.Black)) {
                    val exo = remember(pickedUri) {
                        ExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(pickedUri!!))
                            prepare()
                        }
                    }
                    DisposableEffect(pickedUri) { onDispose { exo.release() } }
                    AndroidView(factory = { PlayerView(context).apply { player = exo } }, modifier = Modifier.fillMaxSize())
                }
                TextButton(onClick = { pickedUri = null }) { Text("إزالة ✕", color = MaterialTheme.colorScheme.error) }
            }

            Spacer(Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterToggle("فيديو عادي", !isShort) { isShort = false }
                FilterToggle("Short", isShort) { isShort = true }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("العنوان") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("الوصف") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = hashtags, onValueChange = { hashtags = it }, label = { Text("الوسوم (Hashtags)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(18.dp))

            val isSaving = vm.publishState is VideoViewModel.PublishState.Saving
            Button(
                onClick = {
                    pickedUri?.let { uri ->
                        vm.publish(uri, title, description, hashtags, durationLabel = "00:00", isShort = isShort)
                    }
                },
                enabled = pickedUri != null && !isSaving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Emerald)
            ) {
                if (isSaving) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                else Text("نشر", fontWeight = FontWeight.Bold)
            }

            (vm.publishState as? VideoViewModel.PublishState.Error)?.let {
                Spacer(Modifier.height(8.dp))
                Text(it.message, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun FilterToggle(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Emerald else MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 9.dp)
    ) {
        Text(label, color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}
