package com.rohaniyat.app.ui.screens.adhkar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohaniyat.app.data.repository.AdhkarRepository
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.QuranTextStyle
import com.rohaniyat.app.viewmodel.AdhkarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdhkarRunnerScreen(categoryId: String, onBack: () -> Unit) {
    val vm: AdhkarViewModel = viewModel()
    val category = AdhkarRepository.byId(categoryId) ?: return
    var index by remember { mutableIntStateOf(0) }
    val item = category.items[index]
    val count = vm.countFor(categoryId, index)

    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(category.title) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowForward, contentDescription = "رجوع") } },
            actions = { Text("${index + 1}/${category.items.size}", modifier = Modifier.padding(end = 16.dp), fontSize = 12.sp) }
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            shape = RoundedCornerShape(22.dp)
        ) {
            Column(Modifier.padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(item.text, style = QuranTextStyle, textAlign = TextAlign.Center)
                Spacer(Modifier.height(18.dp))
                Text("$count / ${item.repeatCount}", fontSize = 34.sp, fontWeight = FontWeight.Black, color = Emerald)
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = { vm.tap(categoryId, index, item.repeatCount) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Emerald)
                ) { Text("اضغط للعد", fontWeight = FontWeight.Bold) }
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(onClick = { if (index > 0) index-- }) { Text("‹ السابق") }
                    TextButton(onClick = { vm.reset(categoryId, index) }) { Text("إعادة الضبط") }
                    TextButton(onClick = { if (index < category.items.size - 1) index++ }) { Text("التالي ›") }
                }
            }
        }
    }
}
