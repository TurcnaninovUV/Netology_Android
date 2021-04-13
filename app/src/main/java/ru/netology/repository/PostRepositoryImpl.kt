package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity

class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll(): LiveData<List<Post>> = dao.getAll().map { it ->
        it.map {
            it.toPost()
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun repostById(id: Long) {
        dao.repostById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}

