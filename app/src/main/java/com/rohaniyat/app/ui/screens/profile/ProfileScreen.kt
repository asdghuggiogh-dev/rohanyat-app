package com.rohaniyat.app.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.rohaniyat.app.data.model.formatViewsArabic
import com.rohaniyat.app.ui.theme.*
import com.rohaniyat.app.viewmodel.ProfileViewModel
import com.rohaniyat.app.viewmodel.VideoViewModel

private enum class ProfileTab(val label: String) { VIDEOS("الفيديوهات"), SHORTS("Shorts"), REPOSTS("إعادة النشر"), LIKES("الإعجابات"), SAVED("المحفوظات") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onOpenSettings: () -> Unit, onOpenVideo: (Long) -> Unit) {
    val profileVm: ProfileViewModel = viewModel()
    val videoVm: VideoViewModel = viewModel()
    val videos by videoVm.videos.collectAsState()
    var tab by remember { mutableStateOf(ProfileTab.VIDEOS) }
    var showEditSheet by remember { mutableStateOf(false) }

    val pickAvatar = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let { profileVm.setAvatar(it) }
    }
    val pickCover = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        uri?.let { profileVm.setCover(it) }
    }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Box(
            Modifier.fillMaxWidth().height(130.dp)
                .background(Brush.linearGradient(listOf(EmeraldDark, Gold)))
        ) {
            profileVm.coverPath?.let {
                Image(rememberAsyncImagePainter(it), contentDescription = "الغلاف", modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
            }
            IconButton(onClick = onOpenSettings, modifier = Modifier.align(Alignment.TopStart).padding(10.dp)) {
                Icon(Icons.Default.Settings, contentDescription = "الإعدادات", tint = Color.White)
            }
        }

        Column(Modifier.padding(horizontal = 20.dp).offset(y = (-42).dp)) {
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .border(3.dp, Brush.linearGradient(listOf(Gold, EmeraldLight)), CircleShape)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .background(Ivory)
                    .clickable { pickAvatar.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                contentAlignment = Alignment.Center
            ) {
                if (profileVm.avatarPath != null) {
                    Image(rememberAsyncImagePainter(profileVm.avatarPath), contentDescription = "الصورة الشخصية", modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                } else {
                    Text("🧕", fontSize = 32.sp)
                }
                Box(
                    Modifier.align(Alignment.BottomEnd).size(26.dp).clip(CircleShape).background(Emerald),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.CameraAlt, contentDescription = "تغيير", tint = Color.White, modifier = Modifier.size(14.dp)) }
            }

            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("نور القرآن", fontWeight = FontWeight.Black, fontSize = 19.sp)
                Spacer(Modifier.width(4.dp))
                Icon(Icons.Default.Verified, contentDescription = "موثّق", tint = Emerald, modifier = Modifier.size(17.dp))
            }
            Text("@noor_alquran", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))

            var bioExpanded by remember { mutableStateOf(false) }
            val bioShort = "قناة تهتم بتلاوة القرآن الكريم وتدبّر معانيه"
            val bioFull = "$bioShort، ونشر محتوى إيماني هادئ يوميًا، مع تفريغ لبعض الدروس المختارة وتذكيرات روحانية قصيرة 🌙"
            Text(
                (if (bioExpanded) bioFull else "$bioShort...") ,
                fontSize = 13.5.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 10.dp)
            )
            Text(
                if (bioExpanded) "أقل" else "المزيد",
                color = Emerald, fontWeight = FontWeight.Bold, fontSize = 12.sp,
                modifier = Modifier.clickable { bioExpanded = !bioExpanded }
            )

            Row(Modifier.padding(top = 14.dp), horizontalArrangement = Arrangement.spacedBy(22.dp)) {
                StatItem("41M", "مشاهدات")
                StatItem("3.2M", "إعجاب")
                StatItem("54", "متابَعون")
                StatItem("128K", "مشتركون")
            }

            Row(Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { showEditSheet = true }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Emerald)) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(6.dp)); Text("تعديل القناة")
                }
                OutlinedButton(onClick = { }) { Icon(Icons.Default.BarChart, contentDescription = null, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(6.dp)); Text("الإحصاءات") }
                OutlinedButton(onClick = { }) { Icon(Icons.Default.Share, contentDescription = "مشاركة") }
            }

            ScrollableTabRow(selectedTabIndex = tab.ordinal, edgePadding = 0.dp, modifier = Modifier.padding(top = 16.dp)) {
                ProfileTab.entries.forEach { t ->
                    Tab(selected = tab == t, onClick = { tab = t }, text = { Text(t.label, fontSize = 11.5.sp) })
                }
            }
        }

        when (tab) {
            ProfileTab.VIDEOS -> {
                val mine = videos.filter { it.isMine && !it.isShort }
                if (mine.isEmpty()) EmptyState("🎬", "لا توجد فيديوهات بعد", "انشر أول فيديو من زر \"إنشاء\"")
                else ProfileGrid(mine.map { it.id to it.title }, onOpenVideo)
            }
            ProfileTab.SHORTS -> {
                val mine = videos.filter { it.isMine && it.isShort }
                if (mine.isEmpty()) EmptyState("▶️", "لا توجد Shorts بعد", "انشر أول Short من زر \"إنشاء\"")
                else ProfileGrid(mine.map { it.id to it.title }, onOpenVideo)
            }
            ProfileTab.REPOSTS -> EmptyState("🔁", "لا توجد إعادة نشر بعد", "الفيديوهات المعاد نشرها ستظهر هنا")
            ProfileTab.LIKES -> EmptyState("♡", "لا يوجد محتوى بعد", "الفيديوهات التي تعجبك ستظهر هنا")
            ProfileTab.SAVED -> EmptyState("🔖", "لا يوجد محتوى بعد", "العناصر المحفوظة ستظهر هنا")
        }
    }

    if (showEditSheet) {
        ModalBottomSheet(onDismissRequest = { showEditSheet = false }) {
            Column(Modifier.padding(20.dp)) {
                Text("تعديل القناة", fontWeight = FontWeight.Black, fontSize = 17.sp)
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = { pickAvatar.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        modifier = Modifier.weight(1f)
                    ) { Text("🖼️ الصورة الشخصية", fontSize = 12.sp) }
                    OutlinedButton(
                        onClick = { pickCover.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
                        modifier = Modifier.weight(1f)
                    ) { Text("🏞️ صورة الغلاف", fontSize = 12.sp) }
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = "نور القرآن", onValueChange = {}, label = { Text("اسم القناة") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = "noor_alquran", onValueChange = {}, label = { Text("المعرف") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                Button(onClick = { showEditSheet = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Emerald)) { Text("حفظ") }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontWeight = FontWeight.Black, fontSize = 15.sp)
        Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
private fun EmptyState(icon: String, title: String, subtitle: String) {
    Box(Modifier.fillMaxWidth().padding(vertical = 60.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(icon, fontSize = 34.sp)
            Spacer(Modifier.height(10.dp))
            Text(title, fontWeight = FontWeight.Bold)
            Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}

@Composable
private fun ProfileGrid(items: List<Pair<Long, String>>, onOpenVideo: (Long) -> Unit) {
    LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.fillMaxWidth().height(300.dp)) {
        items(items) { (id, title) ->
            Box(
                Modifier
                    .aspectRatio(9f / 14f)
                    .background(Brush.linearGradient(listOf(EmeraldLight, EmeraldDark)))
                    .clickable { onOpenVideo(id) }
                    .padding(4.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(title, color = Color.White, fontSize = 10.sp, maxLines = 2)
            }
        }
    }
}
