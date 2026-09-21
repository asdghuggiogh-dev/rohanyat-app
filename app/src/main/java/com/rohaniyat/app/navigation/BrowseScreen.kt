package com.rohaniyat.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class BrowseTool(val icon: String, val title: String, val subtitle: String, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onOpenQuran: () -> Unit,
    onOpenAdhkar: () -> Unit,
    onOpenDuas: () -> Unit,
    onOpenTasbih: () -> Unit,
    onOpenVideos: () -> Unit
) {
    val tools = listOf(
        BrowseTool("📖", "القرآن الكريم", "114 سورة", onOpenQuran),
        BrowseTool("📿", "الأذكار", "7 أقسام", onOpenAdhkar),
        BrowseTool("🤲", "الأدعية", "مكتبة أدعية", onOpenDuas),
        BrowseTool("📿", "المسبحة", "عداد إلكتروني", onOpenTasbih),
        BrowseTool("🎬", "الفيديوهات", "محتوى روحاني", onOpenVideos)
    )

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("تصفح") })
        LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(18.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tools) { tool ->
                Column(
                    Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .clickable(onClick = tool.onClick)
                        .padding(16.dp)
                ) {
                    Text(tool.icon, fontSize = 24.sp)
                    Spacer(Modifier.height(8.dp))
                    Text(tool.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(tool.subtitle, fontSize = 11.5.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        }
    }
}
