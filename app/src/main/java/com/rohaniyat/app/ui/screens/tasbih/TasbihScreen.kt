package com.rohaniyat.app.ui.screens.tasbih

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.EmeraldDark
import com.rohaniyat.app.ui.theme.EmeraldLight
import com.rohaniyat.app.ui.theme.GoldSoft
import com.rohaniyat.app.viewmodel.TasbihViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen() {
    val vm: TasbihViewModel = viewModel()

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("المسبحة الإلكترونية") })
        Column(
            Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var expanded by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
            Box {
                OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(vm.selectedDhikr, fontSize = 17.sp)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    vm.dhikrOptions.forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = { vm.selectedDhikr = option; expanded = false })
                    }
                }
            }
            Spacer(Modifier.height(26.dp))
            Box(
                modifier = Modifier.size(220.dp).border(3.dp, SolidColor(GoldSoft), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${vm.count}", fontSize = 56.sp, fontWeight = FontWeight.Black, color = Emerald)
                    Text("من ${vm.target}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
            Spacer(Modifier.height(26.dp))
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .background(Brush.radialGradient(listOf(EmeraldLight, EmeraldDark)), CircleShape)
                    .clickable { vm.tap() },
                contentAlignment = Alignment.Center
            ) {
                Text("اضغط للتسبيح", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.height(20.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(onClick = { vm.setTarget(33) }, modifier = Modifier.weight(1f)) { Text("33") }
                OutlinedButton(onClick = { vm.setTarget(99) }, modifier = Modifier.weight(1f)) { Text("99") }
                OutlinedButton(onClick = { vm.setTarget(100) }, modifier = Modifier.weight(1f)) { Text("100") }
                OutlinedButton(onClick = { vm.reset() }, modifier = Modifier.weight(1f)) { Text("↺") }
            }
        }
    }
}
