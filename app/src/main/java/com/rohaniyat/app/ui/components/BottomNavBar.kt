package com.rohaniyat.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohaniyat.app.navigation.Screen
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.EmeraldLight

data class NavItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

val bottomNavItems = listOf(
    NavItem(Screen.Home.route, "الرئيسية", Icons.Default.Home),
    NavItem(Screen.Browse.route, "تصفح", Icons.Default.Explore),
    NavItem(Screen.Create.route, "إنشاء", Icons.Default.Add),
    NavItem(Screen.Shorts.route, "Shorts", Icons.Default.PlayArrow),
    NavItem(Screen.Profile.route, "حسابي", Icons.Default.Person)
)

@Composable
fun RohaniyatBottomBar(currentRoute: String?, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route
            if (item.route == Screen.Create.route) {
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    icon = {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Brush.linearGradient(listOf(Emerald, EmeraldLight))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = item.label, tint = androidx.compose.ui.graphics.Color.White)
                        }
                    },
                    label = { Text(item.label, fontSize = 10.sp) }
                )
            } else {
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.route) },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label, fontSize = 10.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }
    }
}
