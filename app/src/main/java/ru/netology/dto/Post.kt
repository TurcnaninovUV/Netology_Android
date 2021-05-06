package ru.netology.dto

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize



 data class Post(
        val id: Long,
        val author: String,
        val content: String,
        val published: String,
        val likes: Int = 0,
        val repost: Int = 0,
        val likedByMe: Boolean = true,
        val video: String? = null
)


