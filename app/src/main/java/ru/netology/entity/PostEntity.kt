package ru.netology.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Attachment
import ru.netology.dto.AttachmentType
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
        val readIt: Boolean = false,
        @Embedded
        var attachment: AttachmentEmbeddable?,
) {
    fun toDto() =
            Post(
                    id,
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
                        dto.readIt,
                        AttachmentEmbeddable.fromDto(dto.attachment)
                )
    }
}

data class AttachmentEmbeddable(
        var url: String,
        var type: AttachmentType,
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
