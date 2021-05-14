package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun repostById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)

    fun likeByIdAsync(callback: LikeAndRepostByIdCallback, id: Long)
    fun repostByIdAsync(callback: LikeAndRepostByIdCallback, id: Long)
    fun removeByIdAsync(callback: LikeAndRepostByIdCallback, id: Long)

    fun saveAsync(callback: SaveCallback, post: Post)

    interface SaveCallback {
        fun onSuccess(post: Post) {}
        fun onError(e: Exception) {}
    }

    interface LikeAndRepostByIdCallback {
        fun onSuccess(id: Long) {}
        fun onError(e: Exception) {}
    }

    fun getAllAsync(callback: GetAllCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>) {}
        fun onError(e: Exception) {}
    }
}
