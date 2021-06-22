package ru.netology.dto


data class Post(
        val id: Long,
        val author: String,
        val authorAvatar: String,
        val content: String,
        val published: String,
        val likes: Int = 0,
        val repost: Int = 0,
        val likedByMe: Boolean = true,
        val video: String? = null,
        val readIt : Boolean = false,
        var attachment: Attachment? = null,
)





