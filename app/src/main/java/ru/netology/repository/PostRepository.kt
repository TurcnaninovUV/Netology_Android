package ru.netology.repository

import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>
    suspend fun getAll()
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    suspend fun repostById(id: Long)

//    fun getAllAsync(callback: Callback<List<Post>>)
//    fun likeById(id: Long, callback: Callback<Post>)
//    fun repostById(id: Long)
//    fun removeById(id: Long, callback: Callback<Unit>)
//    fun save(post: Post, callback: Callback<Post>)
//    fun dislikeById(id: Long, callback: Callback<Post>)
//
//
//    interface Callback<T> {
//        fun onSuccess(posts: T) {}
//        fun onError(e: Exception) {}
//    }
}

