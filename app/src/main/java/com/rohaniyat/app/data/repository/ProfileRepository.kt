package com.rohaniyat.app.data.repository

import com.rohaniyat.app.data.local.ProfileImageDao
import com.rohaniyat.app.data.local.ProfileImageEntity

class ProfileRepository(private val dao: ProfileImageDao) {
    suspend fun getAvatarPath(): String? = dao.get("avatar")?.filePath
    suspend fun getCoverPath(): String? = dao.get("cover")?.filePath

    suspend fun setAvatarPath(path: String) = dao.upsert(ProfileImageEntity("avatar", path))
    suspend fun setCoverPath(path: String) = dao.upsert(ProfileImageEntity("cover", path))
}
