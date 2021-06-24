package ru.netology.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.dto.Media
import ru.netology.dto.MediaUpload
import ru.netology.dto.Post

interface PostRepository {
    val data: Flow<List<Post>>
    suspend fun getAll()
    suspend fun getPostsReadIt()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun dislikeById(id: Long)
    suspend fun repostById(id: Long)
    suspend fun upload(upload: MediaUpload): Media
    suspend fun authentication(login: String, pass: String)
    suspend fun registration(name: String, login: String, pass: String)

}

