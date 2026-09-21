package com.rohaniyat.app.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rohaniyat.app.ui.components.RohaniyatBottomBar
import com.rohaniyat.app.ui.screens.adhkar.AdhkarRunnerScreen
import com.rohaniyat.app.ui.screens.adhkar.AdhkarScreen
import com.rohaniyat.app.ui.screens.create.CreateScreen
import com.rohaniyat.app.ui.screens.duas.DuasScreen
import com.rohaniyat.app.ui.screens.home.HomeScreen
import com.rohaniyat.app.ui.screens.onboarding.OnboardingScreen
import com.rohaniyat.app.ui.screens.profile.ProfileScreen
import com.rohaniyat.app.ui.screens.quran.QuranListScreen
import com.rohaniyat.app.ui.screens.quran.QuranReaderScreen
import com.rohaniyat.app.ui.screens.search.SearchScreen
import com.rohaniyat.app.ui.screens.settings.SettingsScreen
import com.rohaniyat.app.ui.screens.shorts.ShortsScreen
import com.rohaniyat.app.ui.screens.tasbih.TasbihScreen
import com.rohaniyat.app.ui.screens.videoplayer.VideoPlayerScreen
import com.rohaniyat.app.ui.screens.videos.VideosScreen

private val bottomBarRoutes = setOf(Screen.Home.route, Screen.Browse.route, Screen.Create.route, Screen.Shorts.route, Screen.Profile.route, Screen.QuranList.route, Screen.Videos.route)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RohaniyatNavGraph(darkMode: Boolean, onDarkModeChange: (Boolean) -> Unit) {
    val navController = rememberNavController()
    var hasEntered by remember { mutableStateOf(false) }
    var showVideoOptions by remember { mutableStateOf(false) }

    if (!hasEntered) {
        OnboardingScreen(onEnter = { hasEntered = true })
        return
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                RohaniyatBottomBar(currentRoute = currentRoute) { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(if (currentRoute in bottomBarRoutes) padding else androidx.compose.foundation.layout.PaddingValues(0.dp))
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onOpenSearch = { navController.navigate(Screen.Search.route) },
                    onOpenSurah = { navController.navigate(Screen.QuranReader.with(it)) },
                    onOpenAdhkarCategory = { navController.navigate(Screen.AdhkarRunner.with(it)) },
                    onSeeAllQuran = { navController.navigate(Screen.QuranList.route) },
                    onSeeAllVideos = { navController.navigate(Screen.Videos.route) },
                    onOpenVideo = { navController.navigate(Screen.VideoPlayer.with(it)) },
                    onOpenVideoOptions = { showVideoOptions = true }
                )
            }
            composable(Screen.Browse.route) {
                BrowseScreen(
                    onOpenQuran = { navController.navigate(Screen.QuranList.route) },
                    onOpenAdhkar = { navController.navigate(Screen.Adhkar.route) },
                    onOpenDuas = { navController.navigate(Screen.Duas.route) },
                    onOpenTasbih = { navController.navigate(Screen.Tasbih.route) },
                    onOpenVideos = { navController.navigate(Screen.Videos.route) }
                )
            }
            composable(Screen.Create.route) {
                CreateScreen(onPublished = { isShort ->
                    navController.navigate(if (isShort) Screen.Shorts.route else Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                })
            }
            composable(Screen.Shorts.route) { ShortsScreen() }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onOpenSettings = { navController.navigate(Screen.Settings.route) },
                    onOpenVideo = { navController.navigate(Screen.VideoPlayer.with(it)) }
                )
            }
            composable(Screen.QuranList.route) {
                QuranListScreen(
                    onBack = { navController.popBackStack() },
                    onOpenSurah = { navController.navigate(Screen.QuranReader.with(it)) }
                )
            }
            composable(
                Screen.QuranReader.route,
                arguments = listOf(navArgument("surahNumber") { type = androidx.navigation.NavType.IntType })
            ) { entry ->
                val n = entry.arguments?.getInt("surahNumber") ?: 1
                QuranReaderScreen(surahNumber = n, onBack = { navController.popBackStack() })
            }
            composable(Screen.Adhkar.route) {
                AdhkarScreen(onOpenCategory = { navController.navigate(Screen.AdhkarRunner.with(it)) })
            }
            composable(
                Screen.AdhkarRunner.route,
                arguments = listOf(navArgument("categoryId") { type = androidx.navigation.NavType.StringType })
            ) { entry ->
                val id = entry.arguments?.getString("categoryId") ?: "morning"
                AdhkarRunnerScreen(categoryId = id, onBack = { navController.popBackStack() })
            }
            composable(Screen.Duas.route) { DuasScreen() }
            composable(Screen.Tasbih.route) { TasbihScreen() }
            composable(Screen.Videos.route) {
                VideosScreen(
                    onOpenVideo = { navController.navigate(Screen.VideoPlayer.with(it)) },
                    onOpenSearch = { navController.navigate(Screen.Search.route) },
                    onOpenVideoOptions = { showVideoOptions = true }
                )
            }
            composable(
                Screen.VideoPlayer.route,
                arguments = listOf(navArgument("videoId") { type = androidx.navigation.NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("videoId") ?: 0L
                VideoPlayerScreen(
                    videoId = id,
                    onBack = { navController.popBackStack() },
                    onOpenAnotherVideo = { navController.navigate(Screen.VideoPlayer.with(it)) },
                    onOpenOptions = { showVideoOptions = true }
                )
            }
            composable(Screen.Search.route) {
                SearchScreen(
                    onBack = { navController.popBackStack() },
                    onOpenSurah = { navController.navigate(Screen.QuranReader.with(it)) },
                    onOpenVideo = { navController.navigate(Screen.VideoPlayer.with(it)) }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen(darkMode = darkMode, onDarkModeChange = onDarkModeChange, onBack = { navController.popBackStack() })
            }
        }
    }

    if (showVideoOptions) {
        ModalBottomSheet(onDismissRequest = { showVideoOptions = false }) {
            androidx.compose.foundation.layout.Column(Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                OptionRow("🔖", "حفظ") { showVideoOptions = false }
                OptionRow("↗", "مشاركة") { showVideoOptions = false }
                OptionRow("🚫", "عدم الاهتمام") { showVideoOptions = false }
                OptionRow("⚠️", "إبلاغ") { showVideoOptions = false }
            }
        }
    }
}

@Composable
private fun OptionRow(icon: String, label: String, onClick: () -> Unit) {
    androidx.compose.foundation.layout.Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp)
    ) {
        androidx.compose.material3.Text("$icon   $label")
    }
}
