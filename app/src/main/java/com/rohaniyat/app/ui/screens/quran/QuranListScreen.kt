package com.rohaniyat.app.ui.screens.quran

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.Ivory
import com.rohaniyat.app.viewmodel.QuranViewModel
import com.rohaniyat.app.viewmodel.SurahFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranListScreen(onBack: () -> Unit, onOpenSurah: (Int) -> Unit) {
    val vm: QuranViewModel = viewModel()
    val query by vm.searchQuery
    val filter by vm.filter

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("القرآن الكريم") },
            navigationIcon = {},
            actions = { IconButton(onClick = onBack) { Icon(Icons.Default.Close, contentDescription = "إغلاق") } }
        )
        OutlinedTextField(
            value = query,
            onValueChange = { vm.searchQuery.value = it },
            placeholder = { Text("ابحث عن سورة أو آية...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 8.dp),
            shape = RoundedCornerShape(14.dp),
            singleLine = true
        )
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip("الكل", filter == SurahFilter.ALL) { vm.filter.value = SurahFilter.ALL }
            FilterChip("مكية", filter == SurahFilter.MECCAN) { vm.filter.value = SurahFilter.MECCAN }
            FilterChip("مدنية", filter == SurahFilter.MEDINAN) { vm.filter.value = SurahFilter.MEDINAN }
            FilterChip("المفضلة", filter == SurahFilter.FAVORITES) { vm.filter.value = SurahFilter.FAVORITES }
        }

        val surahs = vm.visibleSurahs()
        if (surahs.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("لا توجد نتائج", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            }
        } else {
            LazyColumn {
                items(surahs, key = { it.number }) { surah ->
                    val isFav = vm.favoriteSurahs.contains(surah.number)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenSurah(surah.number) }
                            .padding(horizontal = 18.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(Ivory),
                            contentAlignment = Alignment.Center
                        ) { Text("${surah.number}", color = Emerald, fontWeight = FontWeight.Bold, fontSize = 13.sp) }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("سورة ${surah.name}", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(
                                "${surah.ayahCount} آية · ${if (surah.isMeccan) "مكية" else "مدنية"}",
                                fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        IconButton(onClick = { vm.toggleFavorite(surah.number) }) {
                            Icon(
                                if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "مفضلة",
                                tint = if (isFav) Color(0xFFB3492F) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg = if (selected) Emerald else MaterialTheme.colorScheme.surface
    val fg = if (selected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(label, color = fg, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
    }
}
