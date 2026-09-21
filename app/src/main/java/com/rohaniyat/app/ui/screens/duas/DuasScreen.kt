package com.rohaniyat.app.ui.screens.duas

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohaniyat.app.data.repository.DuaRepository
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.QuranTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuasScreen() {
    var selected by remember { mutableStateOf(DuaRepository.categories.first().id) }
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("الأدعية") })
        LazyRow(contentPadding = PaddingValues(horizontal = 18.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(DuaRepository.categories) { cat ->
                val isSelected = selected == cat.id
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isSelected) Emerald else MaterialTheme.colorScheme.surface)
                        .clickable { selected = cat.id }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(cat.title, color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        val duas = DuaRepository.categories.firstOrNull { it.id == selected }?.duas.orEmpty()
        Column(Modifier.verticalScroll(rememberScrollState())) {
        duas.forEach { dua ->
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp), shape = RoundedCornerShape(20.dp)) {
                Column(Modifier.padding(20.dp)) {
                    Text(dua.text, style = QuranTextStyle, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        TextButton(onClick = { clipboard.setText(AnnotatedString(dua.text)) }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(4.dp)); Text("نسخ", fontSize = 12.sp)
                        }
                        TextButton(onClick = {
                            val send = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, dua.text) }
                            context.startActivity(Intent.createChooser(send, null))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(4.dp)); Text("مشاركة", fontSize = 12.sp)
                        }
                        TextButton(onClick = { }) {
                            Icon(Icons.Default.FavoriteBorder, contentDescription = null, modifier = Modifier.size(15.dp)); Spacer(Modifier.width(4.dp)); Text("مفضلة", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
        }
    }
}
