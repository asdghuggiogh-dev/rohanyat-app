package com.rohaniyat.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohaniyat.app.data.model.Video
import com.rohaniyat.app.data.model.formatViewsArabic
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.EmeraldLight
import com.rohaniyat.app.ui.theme.Gold
import com.rohaniyat.app.ui.theme.GoldSoft

@Composable
fun VideoCard(
    video: Video,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(bottom = 18.dp)) {
        // Thumbnail
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(Brush.linearGradient(listOf(Emerald, Gold)))
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.size(52.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "تشغيل", tint = Color(0xFF094A39))
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color.Black.copy(alpha = 0.65f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(video.durationLabel, color = Color.White, fontSize = 11.sp)
            }
        }

        // Meta row
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            val avGradient = if (video.avatarColorIsGold) listOf(Gold, GoldSoft) else listOf(Emerald, EmeraldLight)
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(avGradient))
                    .clickable(onClick = onClick),
                contentAlignment = Alignment.Center
            ) {
                Text(video.channelName.take(1), color = Color.White, fontWeight = FontWeight.Bold)
            }

            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 10.dp).clickable(onClick = onClick)
            ) {
                Text(video.title, fontWeight = FontWeight.Bold, fontSize = 14.5.sp, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(video.channelName, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    if (video.verified) {
                        Spacer(Modifier.width(3.dp))
                        Icon(Icons.Default.Verified, contentDescription = "موثّق", tint = Emerald, modifier = Modifier.size(12.dp))
                    }
                    Text("  •  ${formatViewsArabic(video.viewCount)}  •  منذ ${video.relativeTime}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }

            IconButton(onClick = onMoreClick, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.MoreVert, contentDescription = "خيارات")
            }
        }
    }
}
