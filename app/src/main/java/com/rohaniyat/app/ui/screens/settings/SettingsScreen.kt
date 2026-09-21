package com.rohaniyat.app.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    darkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("الإعدادات") },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowForward, contentDescription = "رجوع") } }
        )
        SettingsToggleRow("الوضع الداكن", darkMode, onDarkModeChange)
        SettingsRow("🌐", "اللغة: العربية")
        SettingsRow("🔔", "إعدادات الإشعارات")
        SettingsRow("🔒", "الخصوصية")
        SettingsRow("👤", "الحساب")
        SettingsRow("🔑", "كلمة المرور")
        SettingsRow("♡", "المفضلة")
        SettingsRow("👁", "سجل المشاهدة")
        SettingsRow("🎧", "سجل الاستماع")
        SettingsRow("🚪", "تسجيل الخروج", isDestructive = true)
    }
}

@Composable
private fun SettingsToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp)
        Switch(checked = checked, onCheckedChange = onChange)
    }
    Divider()
}

@Composable
private fun SettingsRow(icon: String, label: String, isDestructive: Boolean = false) {
    Row(
        Modifier.fillMaxWidth().clickable { }.padding(horizontal = 18.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(30.dp))
        Text(label, fontSize = 14.5.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f), color = if (isDestructive) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
        Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
    }
    Divider()
}
