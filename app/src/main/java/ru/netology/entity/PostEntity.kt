package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val author: String,
        val authorAvatar: String,
        val content: String,
        val published: String,
        val likes: Int = 0,
        val repost: Int = 0,
        val likedByMe: Boolean,
        val video: String?,
        val readIt : Boolean
) {
    fun toDto() =
        Post(id, author, authorAvatar, content, published, likes, repost, likedByMe, video, readIt)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                    dto.id,
                    dto.author,
                    dto.authorAvatar,
                    dto.content,
                    dto.published,
                    dto.likes,
                    dto.repost,
                    dto.likedByMe,
                    dto.video,
                    dto.readIt
            )
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
