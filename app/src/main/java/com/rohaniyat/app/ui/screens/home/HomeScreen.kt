package com.rohaniyat.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohaniyat.app.data.repository.QuranRepository
import com.rohaniyat.app.ui.components.VideoCard
import com.rohaniyat.app.ui.theme.*
import com.rohaniyat.app.viewmodel.VideoViewModel

@Composable
fun HomeScreen(
    onOpenSearch: () -> Unit,
    onOpenSurah: (Int) -> Unit,
    onOpenAdhkarCategory: (String) -> Unit,
    onSeeAllQuran: () -> Unit,
    onSeeAllVideos: () -> Unit,
    onOpenVideo: (Long) -> Unit,
    onOpenVideoOptions: () -> Unit
) {
    val vm: VideoViewModel = viewModel()
    val videos by vm.videos.collectAsState()

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        // Top bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(34.dp).clip(RoundedCornerShape(9.dp))
                        .background(Brush.linearGradient(listOf(Emerald, Gold))),
                    contentAlignment = Alignment.Center
                ) { Text("🌙", fontSize = 15.sp) }
                Spacer(Modifier.width(10.dp))
                Text("روحانيات", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            }
            Row {
                IconButton(onClick = onOpenSearch) { Icon(Icons.Default.Search, contentDescription = "بحث") }
                IconButton(onClick = { /* notifications */ }) { Icon(Icons.Default.Notifications, contentDescription = "إشعارات") }
            }
        }

        Text("أهلًا بك من جديد", modifier = Modifier.padding(horizontal = 18.dp), fontSize = 22.sp, fontWeight = FontWeight.Black)
        Spacer(Modifier.height(14.dp))

        // Ayah of the day hero card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Brush.linearGradient(listOf(EmeraldDark, Emerald)))
                .padding(22.dp)
        ) {
            Column {
                Text("آية اليوم", color = GoldSoft, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("وَبَشِّرِ الصَّابِرِينَ", color = Color.White, fontSize = 22.sp)
                Spacer(Modifier.height(10.dp))
                Text("سورة البقرة - الآية 155", color = Color.White.copy(alpha = 0.75f), fontSize = 12.sp)
            }
        }

        SectionHeader("أذكار اليوم")
        LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(listOf(Triple("morning", "أذكار الصباح", "18 ذكرًا"), Triple("evening", "أذكار المساء", "17 ذكرًا"), Triple("sleep", "أذكار النوم", "9 أذكار"))) { (id, title, subtitle) ->
                MiniCard(icon = "🌅", title = title, subtitle = subtitle) { onOpenAdhkarCategory(id) }
            }
        }

        SectionHeader("القرآن الكريم", onSeeAll = onSeeAllQuran)
        LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(QuranRepository.surahs.take(4)) { surah ->
                MiniCard(icon = "📖", title = "سورة ${surah.name}", subtitle = "${surah.ayahCount} آية") { onOpenSurah(surah.number) }
            }
        }

        SectionHeader("مقترح لك", onSeeAll = onSeeAllVideos)
        Column(modifier = Modifier.padding(horizontal = 18.dp)) {
            videos.take(2).forEach { video ->
                VideoCard(video = video, onClick = { onOpenVideo(video.id) }, onMoreClick = onOpenVideoOptions)
            }
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun SectionHeader(title: String, onSeeAll: (() -> Unit)? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontSize = 16.5.sp, fontWeight = FontWeight.Bold)
        if (onSeeAll != null) {
            Text("عرض الكل", fontSize = 12.5.sp, color = Emerald, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onSeeAll))
        }
    }
}

@Composable
private fun MiniCard(icon: String, title: String, subtitle: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(118.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier.size(46.dp).clip(CircleShape).background(Ivory),
            contentAlignment = Alignment.Center
        ) { Text(icon, fontSize = 20.sp) }
        Spacer(Modifier.height(8.dp))
        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp, maxLines = 1)
        Text(subtitle, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}
