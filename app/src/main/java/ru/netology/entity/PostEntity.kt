package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = true,
    val likes: Int = 0,
    val repost: Int = 0,
    val video: String? = null

) {
    fun toPost() = Post(
        id = id,
        author = author,
        content = content,
        published = published,
        likedByMe = likedByMe,
        likes = likes,
        repost = repost,
        video = video
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                id = dto.id,
                author = dto.author,
                content = dto.content,
                published = dto.published,
                likedByMe = dto.likedByMe,
                likes = dto.likes,
                repost = dto.repost,
                video = dto.video)
    }
}