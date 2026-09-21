package com.rohaniyat.app.ui.screens.quran

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohaniyat.app.data.repository.QuranRepository
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.Gold
import com.rohaniyat.app.ui.theme.QuranTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranReaderScreen(surahNumber: Int, onBack: () -> Unit) {
    val surah = QuranRepository.surahs.firstOrNull { it.number == surahNumber }
    val ayahs = QuranRepository.ayahsFor(surahNumber)
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val favorites = remember { mutableStateListOf<Int>() }

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("سورة ${surah?.name ?: ""}") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowForward, contentDescription = "رجوع") } }
        )

        if (ayahs.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(
                    "هذه السورة (${surah?.ayahCount ?: 0} آية) غير متاحة في هذا العرض التجريبي.\n" +
                        "يعرض هذا التطبيق نصوصًا مختارة فقط لبعض السور — اربطه بمصدر بيانات قرآن كامل ومرخّص قبل الإطلاق الفعلي.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            return@Column
        }

        LazyColumn(Modifier.padding(horizontal = 20.dp)) {
            if (surahNumber != 9) {
                item {
                    Text(
                        "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        style = QuranTextStyle,
                        color = Emerald,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                    )
                }
            }
            items(ayahs) { ayah ->
                Row(Modifier.padding(bottom = 22.dp)) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .border(1.4.dp, SolidColor(Gold), CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("${ayah.number}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Emerald) }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(ayah.text, style = QuranTextStyle, textAlign = TextAlign.End)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            val isFav = favorites.contains(ayah.number)
                            TextButton(onClick = { if (isFav) favorites.remove(ayah.number) else favorites.add(ayah.number) }) {
                                Icon(Icons.Default.FavoriteBorder, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text(if (isFav) "محفوظة" else "حفظ", fontSize = 12.sp)
                            }
                            TextButton(onClick = {
                                val send = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, ayah.text)
                                }
                                context.startActivity(Intent.createChooser(send, null))
                            }) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("مشاركة", fontSize = 12.sp)
                            }
                            TextButton(onClick = { clipboard.setText(AnnotatedString(ayah.text)) }) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("نسخ", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(60.dp)) }
        }
    }
}
