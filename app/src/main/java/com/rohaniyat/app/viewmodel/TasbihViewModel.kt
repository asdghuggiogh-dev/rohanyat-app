package com.rohaniyat.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TasbihViewModel : ViewModel() {
    var count by mutableIntStateOf(0)
        private set
    var target by mutableIntStateOf(33)
        private set
    var selectedDhikr by mutableStateOf("سُبْحَانَ اللَّهِ")

    val dhikrOptions = listOf("سُبْحَانَ اللَّهِ", "الْحَمْدُ لِلَّهِ", "اللَّهُ أَكْبَرُ", "لَا إِلَٰهَ إِلَّا اللَّهُ", "أَسْتَغْفِرُ اللَّهَ")

    fun tap() { count++ }
    fun reset() { count = 0 }
    fun setTarget(value: Int) { target = value }
}
