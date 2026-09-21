package com.rohaniyat.app.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rohaniyat.app.data.model.Surah
import com.rohaniyat.app.data.repository.QuranRepository

enum class SurahFilter { ALL, MECCAN, MEDINAN, FAVORITES }

class QuranViewModel : ViewModel() {
    val favoriteSurahs = mutableStateListOf<Int>()
    var filter = mutableStateOf(SurahFilter.ALL)
    var searchQuery = mutableStateOf("")

    fun toggleFavorite(surahNumber: Int) {
        if (favoriteSurahs.contains(surahNumber)) favoriteSurahs.remove(surahNumber)
        else favoriteSurahs.add(surahNumber)
    }

    fun visibleSurahs(): List<Surah> {
        var list = QuranRepository.surahs
        list = when (filter.value) {
            SurahFilter.MECCAN -> list.filter { it.isMeccan }
            SurahFilter.MEDINAN -> list.filter { !it.isMeccan }
            SurahFilter.FAVORITES -> list.filter { favoriteSurahs.contains(it.number) }
            SurahFilter.ALL -> list
        }
        if (searchQuery.value.isNotBlank()) {
            list = list.filter { it.name.contains(searchQuery.value) || it.number.toString() == searchQuery.value }
        }
        return list
    }
}
