package com.rohaniyat.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.rohaniyat.app.navigation.RohaniyatNavGraph
import com.rohaniyat.app.ui.theme.RohaniyatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkMode by remember { mutableStateOf(false) }
            RohaniyatTheme(darkTheme = darkMode) {
                Surface {
                    RohaniyatNavGraph(darkMode = darkMode, onDarkModeChange = { darkMode = it })
                }
            }
        }
    }
}
