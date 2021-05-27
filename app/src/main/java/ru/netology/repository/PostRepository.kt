package ru.netology.repository

import ru.netology.dto.Post

interface PostRepository {
    fun getAllAsync(callback: Callback<List<Post>>)
    fun likeById(id: Long, callback: Callback<Post>)
    fun repostById(id: Long)
    fun removeById(id: Long, callback: Callback<Unit>)
    fun save(post: Post, callback: Callback<Post>)
    fun dislikeById(id: Long, callback: Callback<Post>)


    interface Callback<T> {
        fun onSuccess(posts: T) {}
        fun onError(e: Exception) {}
    }
}

