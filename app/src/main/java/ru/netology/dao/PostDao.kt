package ru.netology.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id")
    fun getAll(): LiveData<List<PostEntity>>

    @Insert
    fun insert(post: PostEntity)

    @Query("UPDATE PostEntity SET content = :content WHERE id = :id")
    fun updateContentById(id: Long, content: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(post: PostEntity)


    @Query(
        """
        UPDATE PostEntity SET
        likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
        likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
        WHERE id = :id
        """
    )
    fun likeById(id: Long)

    @Query(
        """
            UPDATE PostEntity SET
            repost = repost + CASE WHEN repost > -1 THEN 1 END
            WHERE id = :id
            """
    )
    fun repostById(id: Long)


    @Query("DELETE FROM PostEntity WHERE id = :id")
    fun removeById(id: Long)
}