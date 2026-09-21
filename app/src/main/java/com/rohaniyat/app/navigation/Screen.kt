package com.rohaniyat.app.navigation

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object Home : Screen("home")
    data object Browse : Screen("browse")
    data object Create : Screen("create")
    data object Shorts : Screen("shorts")
    data object Profile : Screen("profile")
    data object QuranList : Screen("quran_list")
    data object QuranReader : Screen("quran_reader/{surahNumber}") {
        fun with(surahNumber: Int) = "quran_reader/$surahNumber"
    }
    data object Adhkar : Screen("adhkar")
    data object AdhkarRunner : Screen("adhkar_runner/{categoryId}") {
        fun with(categoryId: String) = "adhkar_runner/$categoryId"
    }
    data object Duas : Screen("duas")
    data object Tasbih : Screen("tasbih")
    data object Videos : Screen("videos")
    data object VideoPlayer : Screen("video_player/{videoId}") {
        fun with(videoId: Long) = "video_player/$videoId"
    }
    data object Search : Screen("search")
    data object Settings : Screen("settings")
}
