package com.rohaniyat.app.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohaniyat.app.data.model.formatViewsArabic
import com.rohaniyat.app.data.repository.QuranRepository
import com.rohaniyat.app.viewmodel.VideoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBack: () -> Unit, onOpenSurah: (Int) -> Unit, onOpenVideo: (Long) -> Unit) {
    val vm: VideoViewModel = viewModel()
    var query by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize()) {
        Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowForward, contentDescription = "رجوع") }
            OutlinedTextField(
                value = query, onValueChange = { query = it },
                placeholder = { Text("ابحث عن سور، قراء، فيديوهات، حسابات...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )
        }

        if (query.isBlank()) return@Column

        val surahMatches = remember(query) { QuranRepository.surahs.filter { it.name.contains(query) }.take(5) }
        val videoMatches = remember(query) { vm.searchAll(query) }

        LazyColumn {
            if (surahMatches.isNotEmpty()) {
                item { SectionLabel("سور") }
                items(surahMatches) { s ->
                    ResultRow("📖", "سورة ${s.name}", "${s.ayahCount} آية") { onOpenSurah(s.number) }
                }
            }
            if (videoMatches.isNotEmpty()) {
                item { SectionLabel("فيديوهات") }
                items(videoMatches) { v ->
                    ResultRow("🎬", v.title, "${v.channelName} · ${formatViewsArabic(v.viewCount)}") { onOpenVideo(v.id) }
                }
            }
            if (surahMatches.isEmpty() && videoMatches.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(60.dp), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        Text("لا توجد نتائج", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, fontWeight = FontWeight.Bold, fontSize = 15.sp, modifier = Modifier.padding(18.dp, 14.dp, 18.dp, 4.dp))
}

@Composable
private fun ResultRow(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 20.sp)
        Spacer(Modifier.width(14.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(subtitle, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
        }
    }
}
