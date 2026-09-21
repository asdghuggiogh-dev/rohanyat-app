package com.rohaniyat.app.data.repository

import com.rohaniyat.app.data.local.MediaStorage
import com.rohaniyat.app.data.local.VideoDao
import com.rohaniyat.app.data.local.VideoEntity
import com.rohaniyat.app.data.model.Video
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * The seed/demo entries below have no real media file (id < 1000), matching the project's
 * "no unlicensed media" rule — see README. Anything the user actually publishes is stored
 * for real via [VideoDao] and appears with isMine = true and a working [Video.videoUri].
 */
class VideoRepository(private val videoDao: VideoDao) {

    private val demoVideos = listOf(
        Video(
            id = 1, title = "تفسير سورة الكهف - الحلقة الأولى", channelName = "قناة الهدى",
            verified = true, avatarColorIsGold = false, viewCount = 24300, relativeTime = "يوم واحد",
            durationLabel = "12:40",
            description = "حلقة تفسيرية مبسّطة لسورة الكهف، نستعرض فيها قصص أصحاب الكهف والدروس المستفادة منها."
        ),
        Video(
            id = 2, title = "خواطر إيمانية عن الصبر", channelName = "نور القلوب",
            verified = false, avatarColorIsGold = true, viewCount = 9140, relativeTime = "3 أيام",
            durationLabel = "08:15",
            description = "خاطرة قصيرة عن معنى الصبر الحقيقي وكيف نتعامل مع الابتلاءات بقلب راضٍ."
        ),
        Video(
            id = 3, title = "قصص الأنبياء - سيدنا يوسف عليه السلام", channelName = "قصص من القرآن",
            verified = true, avatarColorIsGold = false, viewCount = 51200, relativeTime = "أسبوع",
            durationLabel = "22:03",
            description = "رحلة مع قصة سيدنا يوسف عليه السلام، من الجُب إلى عرش مصر، وما فيها من عبر وحكم."
        ),
        Video(
            id = 4, title = "فضل قيام الليل", channelName = "د. سالم العتيبي",
            verified = false, avatarColorIsGold = true, viewCount = 6420, relativeTime = "أسبوعان",
            durationLabel = "14:22",
            description = "حديث عن فضل قيام الليل وأثره في حياة المسلم، مع نصائح عملية للمواظبة عليه."
        )
    )

    /** Reactive combined list: demo videos first-seeded, then real uploads newest-first, prepended. */
    fun observeAll(): Flow<List<Video>> = videoDao.observeAll().map { entities ->
        entities.map { it.toVideo() } + demoVideos
    }

    suspend fun getAllOnce(): List<Video> = videoDao.getAll().map { it.toVideo() } + demoVideos

    suspend fun findById(id: Long): Video? = getAllOnce().firstOrNull { it.id == id }

    suspend fun publish(
        title: String,
        description: String,
        hashtags: String,
        filePath: String,
        durationLabel: String,
        isShort: Boolean
    ): Long = videoDao.insert(
        VideoEntity(
            title = title,
            description = description,
            hashtags = hashtags,
            filePath = filePath,
            durationLabel = durationLabel,
            isShort = isShort
        )
    )

    suspend fun registerView(video: Video) {
        // Demo entries (id 1..4, isMine = false) are not backed by a DB row — view counts for
        // them are session-only, exactly like the web prototype's behavior for its seed data.
        if (video.isMine) videoDao.incrementViews(entityIdFor(video.id))
    }

    // Real (Room-backed) ids are offset so they never collide with the small demo id range.
    private fun entityIdFor(videoId: Long) = videoId - ID_OFFSET

    private fun VideoEntity.toVideo() = Video(
        id = id + ID_OFFSET,
        title = title,
        channelName = "نور القرآن",
        verified = true,
        avatarColorIsGold = false,
        viewCount = viewCount,
        relativeTime = "منشور حديثًا",
        durationLabel = durationLabel,
        description = description,
        videoUri = filePath,
        isMine = true,
        isShort = isShort,
        hashtags = hashtags
    )

    fun deleteMedia(filePath: String) = MediaStorage.delete(filePath)

    companion object {
        // Keeps Room's auto-generated ids (which start at 1, same range as demo ids) from
        // ever clashing with the four hard-coded demo video ids above.
        private const val ID_OFFSET = 100_000L
    }
}
