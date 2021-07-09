package ru.netology.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostWorkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likes: Int = 0,
    val repost: Int = 0,
    val likedByMe: Boolean,
    val video: String?,
    val readIt: Boolean = false,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    val uri: String? = null,
) {
    fun toDto() = Post(
        id,
        authorId,
        author,
        authorAvatar,
        content,
        published,
        likes,
        repost,
        likedByMe,
        video,
        readIt,
        attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post, uri: String? = null) =
            PostWorkEntity(
                0L,
                dto.authorId,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likes,
                dto.repost,
                dto.likedByMe,
                dto.video,
                false,
                AttachmentEmbeddable.fromDto(dto.attachment),
                uri
            )
    }
}