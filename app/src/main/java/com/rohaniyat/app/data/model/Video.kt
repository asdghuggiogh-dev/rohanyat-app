package com.rohaniyat.app.data.model

/**
 * A video or short. [videoUri] is null for the built-in demo/sample entries (no real media file
 * is bundled with the app, matching the project's copyright rules — see README). Real uploads
 * always have a non-null [videoUri] pointing at a file this app owns under internal storage.
 */
data class Video(
    val id: Long,
    val title: String,
    val channelName: String,
    val verified: Boolean,
    val avatarColorIsGold: Boolean,
    var viewCount: Long,
    val relativeTime: String,
    val durationLabel: String,
    val description: String = "",
    val videoUri: String? = null,
    val isMine: Boolean = false,
    val isShort: Boolean = false,
    val hashtags: String = ""
)

/** Formats a raw view count using the same Arabic pluralization rules as the web version. */
fun formatViewsArabic(n: Long): String = when {
    n == 0L -> "0 مشاهدة"
    n == 1L -> "1 مشاهدة"
    n == 2L -> "مشاهدتان"
    n in 3..10 -> "$n مشاهدات"
    else -> "${"%,d".format(n)} مشاهدة"
}
