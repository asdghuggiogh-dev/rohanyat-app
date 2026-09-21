package com.rohaniyat.app.data.model

data class Surah(
    val number: Int,
    val name: String,
    val ayahCount: Int,
    val isMeccan: Boolean
)

data class Ayah(
    val number: Int,
    val text: String
)

data class Reciter(val id: String, val name: String)

data class DhikrItem(val text: String, val repeatCount: Int)
data class DhikrCategory(val id: String, val title: String, val items: List<DhikrItem>)

data class Dua(val text: String)
data class DuaCategory(val id: String, val title: String, val duas: List<Dua>)
