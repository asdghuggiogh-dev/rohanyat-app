package com.rohaniyat.app.data.local

import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

/**
 * Copies a media file picked via the system Photo Picker (a content:// Uri, which is only
 * valid for a short-lived permission grant) into a file this app owns under its internal
 * storage. That local file:// path is stable forever, which is what makes an uploaded
 * video/photo survive app restarts and always be playable — the exact problem the web
 * prototype could never fully solve without a server.
 */
object MediaStorage {

    private fun dir(context: Context, sub: String): File =
        File(context.filesDir, sub).apply { if (!exists()) mkdirs() }

    fun copyVideoToInternalStorage(context: Context, source: Uri, extension: String = "mp4"): String {
        val target = File(dir(context, "videos"), "${UUID.randomUUID()}.$extension")
        context.contentResolver.openInputStream(source)?.use { input ->
            target.outputStream().use { output -> input.copyTo(output) }
        }
        return target.absolutePath
    }

    fun copyImageToInternalStorage(context: Context, source: Uri, extension: String = "jpg"): String {
        val target = File(dir(context, "images"), "${UUID.randomUUID()}.$extension")
        context.contentResolver.openInputStream(source)?.use { input ->
            target.outputStream().use { output -> input.copyTo(output) }
        }
        return target.absolutePath
    }

    fun fileSizeMb(path: String): Double = File(path).length() / (1024.0 * 1024.0)

    fun delete(path: String) {
        runCatching { File(path).delete() }
    }
}
