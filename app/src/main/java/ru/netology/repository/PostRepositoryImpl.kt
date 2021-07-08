package ru.netology.repository


import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.netology.api.*
import ru.netology.auth.AppAuth
import ru.netology.dao.PostDao
import ru.netology.dao.PostWorkDao
import ru.netology.dto.*
import ru.netology.entity.PostEntity
import ru.netology.entity.PostWorkEntity
import ru.netology.entity.toDto
import ru.netology.entity.toEntity
import ru.netology.error.ApiError
import ru.netology.error.AppError
import ru.netology.error.NetworkError
import ru.netology.error.UnknownError
import java.io.IOException


class PostRepositoryImpl(
    private val dao: PostDao,
    private val postWorkDao: PostWorkDao
) : PostRepository {
    override val data = dao.getAll()
            .map(List<PostEntity>::toDto)
            .flowOn(Dispatchers.Default)

    override suspend fun getAll() {
        try {
            val response = Api.service.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {
            delay(10_000L)
            val response = Api.service.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
            emit(body.size)
        }
    }
            .catch { e -> throw AppError.from(e) }
            .flowOn(Dispatchers.Default)

    override suspend fun save(post: Post) {
        try {
            val response = Api.service.save(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            // TODO: add support for other types
            val postWithAttachment = post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: AppError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                    "file", upload.file.name, upload.file.asRequestBody()
            )

            val response = Api.service.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun removeById(id: Long) {
        try {
            val response = Api.service.removeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }

    }

    override suspend fun likeById(id: Long) {
        try {
            val response = Api.service.likeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun dislikeById(id: Long) {
        try {
            val response = Api.service.dislikeById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun repostById(id: Long) {
        try {
            val response = Api.service.repostById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun getPostsReadIt() {
        dao.getPostsReadIt()
    }

    override suspend fun authentication(login: String, pass: String) {
        try {
            val response = Api.service.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { AppAuth.getInstance().setAuth(authState.id, it) }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun registration(name: String, login: String, pass: String) {
        try {
            val response = Api.service.registrationUser(name, login, pass)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            authState.token?.let { AppAuth.getInstance().setAuth(authState.id, it) }

        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun saveWork(post: Post, upload: MediaUpload?): Long =
    postWorkDao.insert(PostWorkEntity.fromDto(post, upload?.file?.toUri()?.toString()))


    override suspend fun processWork(id: Long) {
        val postToUpload = postWorkDao.getById(id).toDto()
        if (postToUpload.attachment == null) {
            val response = Api.service.save(postToUpload)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        } else {
            val media = upload(MediaUpload(postToUpload.attachment!!.url.toUri().toFile()))
            val postWithAttachment = postToUpload.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            val response = Api.service.save(postWithAttachment)
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(PostEntity.fromDto(body))
        }
        postWorkDao.removeById(id)
    }

}




