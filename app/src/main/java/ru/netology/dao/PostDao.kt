package ru.netology.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.netology.dto.AttachmentType
import ru.netology.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAll(): Flow<List<PostEntity>>

    @Query("UPDATE PostEntity SET readIt = 1 WHERE readIt = 0")
    suspend fun getPostsReadIt()

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Query("SELECT COUNT(*) FROM PostEntity")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query(
        """
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    suspend fun likeById(id: Long)

    @Query(
        """
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe = 0 THEN -1 END,
        likedByMe = CASE WHEN likedByMe THEN 1 ELSE 0 END
        WHERE id = :id
        """
    )
    suspend fun dislikeById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
            repost = repost + CASE WHEN repost > -1 THEN 1 END
            WHERE id = :id
            """
    )
    suspend fun repostById(id: Long)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Long)
}

class Converters {
    @TypeConverter
    fun toAttachmentType(value: String) = enumValueOf<AttachmentType>(value)

    @TypeConverter
    fun fromAttachmentType(value: AttachmentType) = value.name
}