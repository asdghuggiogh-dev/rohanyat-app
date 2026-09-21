package com.rohaniyat.app.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class AdhkarViewModel : ViewModel() {
    // key = "categoryId-itemIndex"
    val counts = mutableStateMapOf<String, Int>()

    fun countFor(categoryId: String, itemIndex: Int) = counts["$categoryId-$itemIndex"] ?: 0

    fun tap(categoryId: String, itemIndex: Int, target: Int) {
        val key = "$categoryId-$itemIndex"
        val current = counts[key] ?: 0
        counts[key] = (current + 1).coerceAtMost(target)
    }

    fun reset(categoryId: String, itemIndex: Int) {
        counts["$categoryId-$itemIndex"] = 0
    }
}
