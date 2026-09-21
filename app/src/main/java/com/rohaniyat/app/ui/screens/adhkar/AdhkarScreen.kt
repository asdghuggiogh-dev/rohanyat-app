package com.rohaniyat.app.ui.screens.adhkar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohaniyat.app.data.repository.AdhkarRepository
import com.rohaniyat.app.ui.theme.Ivory

private val icons = mapOf(
    "morning" to "🌅", "evening" to "🌇", "sleep" to "🌙", "wake" to "☀️",
    "prayer" to "🕌", "travel" to "🧳", "misc" to "✨"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdhkarScreen(onOpenCategory: (String) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        TopAppBar(title = { Text("الأذكار") })
        AdhkarRepository.categories.forEach { cat ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onOpenCategory(cat.id) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(46.dp).clip(CircleShape).background(Ivory), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.Text(icons[cat.id] ?: "📿", fontSize = 20.sp)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(cat.title, fontWeight = FontWeight.Bold, fontSize = 14.5.sp)
                    Text("${cat.items.size} ذكرًا", fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
                Icon(Icons.Default.ChevronLeft, contentDescription = null)
            }
        }
        Spacer(Modifier.height(60.dp))
    }
}
