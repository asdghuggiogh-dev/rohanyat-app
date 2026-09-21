package com.rohaniyat.app.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rohaniyat.app.ui.theme.Emerald
import com.rohaniyat.app.ui.theme.EmeraldDark
import com.rohaniyat.app.ui.theme.EmeraldLight
import com.rohaniyat.app.ui.theme.Gold
import com.rohaniyat.app.ui.theme.GoldSoft

@Composable
fun OnboardingScreen(onEnter: (isSignup: Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(EmeraldDark, Emerald, EmeraldLight)))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Text("🌙", fontSize = 44.sp)
        }
        Spacer(Modifier.height(28.dp))
        Text(
            "مرحبًا بك في روحانيات",
            color = Color(0xFFFBF8F0),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(10.dp))
        Text(
            "قرآن · تلاوات · أذكار · أدعية · محتوى روحاني",
            color = GoldSoft,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(50.dp))
        Button(
            onClick = { onEnter(true) },
            modifier = Modifier.fillMaxWidth(0.85f).height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color(0xFF1C2620))
        ) { Text("إنشاء حساب", fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = { onEnter(false) },
            modifier = Modifier.fillMaxWidth(0.85f).height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFFBF8F0))
        ) { Text("تصفح كزائر", fontWeight = FontWeight.Bold) }
    }
}
