package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import ru.netology.util.SingleLiveEvent
import java.io.IOException


private val empty = Post(
        id = 0,
        content = "",
        author = "",
        likedByMe = false,
        published = ""
)


class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }


    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAllAsync(object : PostRepository.GetAllCallback {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }


    fun likeById(id: Long) {
        repository.likeByIdAsync(object : PostRepository.LikeAndRepostByIdCallback {
            override fun onSuccess(id: Long) {
                _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                            if (it.id != id) it else it.copy(
                                    likedByMe = !it.likedByMe,
                                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                            )
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }, id)
    }


    fun save() {
        edited.value?.let {
            repository.saveAsync(object : PostRepository.SaveCallback {
                override fun onSuccess(post: Post) {
                    _postCreated.postValue(Unit)
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            }, it)
        }
//        edited.value = empty
    }

//    fun save() {
//        edited.value?.let {
//            thread {
//                repository.save(it)
//                _postCreated.postValue(Unit)
//            }
//        }
//        edited.value = empty
//    }

    fun edit(post: Post) {
        edited.value = post
    }


    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun repostById(id: Long) {
        repository.repostByIdAsync(object : PostRepository.LikeAndRepostByIdCallback {
            override fun onSuccess(id: Long) {
                _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty().map {
                            if (it.id != id) it else it.copy(
                                    repost = it.repost + 1
                            )
                        })
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        }, id)
    }


    fun removeById(id: Long) {
        repository.removeByIdAsync(object : PostRepository.LikeAndRepostByIdCallback {
            override fun onSuccess(id: Long) {
                val old = _data.value?.posts.orEmpty()
                _data.postValue(
                        _data.value?.copy(posts = _data.value?.posts.orEmpty()
                                .filter { it.id != id }
                        )
                )
                try {
                    repository.removeById(id)
                } catch (e: IOException) {
                    _data.postValue(_data.value?.copy(posts = old))
                }
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }

        }, id)
    }
}


//            fun removeById(id: Long) {
//    thread {
//        // Оптимистичная модель
//        val old = _data.value?.posts.orEmpty()
//        _data.postValue(
//                _data.value?.copy(posts = _data.value?.posts.orEmpty()
//                        .filter { it.id != id }
//                )
//        )
//        try {
//            repository.removeById(id)
//        } catch (e: IOException) {
//            _data.postValue(_data.value?.copy(posts = old))
//        }
//    }


//
//fun removeById(id: Long) {
//    thread {
//        // Оптимистичная модель
//        val old = _data.value?.posts.orEmpty()
//        _data.postValue(
//            _data.value?.copy(posts = _data.value?.posts.orEmpty()
//                .filter { it.id != id }
//            )
//        )
//        try {
//            repository.removeById(id)
//        } catch (e: IOException) {
//            _data.postValue(_data.value?.copy(posts = old))
//        }
//    }
//}
//}
//    fun repostById(id: Long) {
//        thread {
//            _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map {
//                if (it.id != id) it else it.copy(
//                        repost = it.repost + 1
//                )
//            })
//            )
//            repository.repostById(id)
//        }
//    }

//    fun likeById(id: Long) {
//        thread {
//            _data.postValue(_data.value?.copy(posts = _data.value?.posts.orEmpty().map {
//                if (it.id != id) it else it.copy(
//                        likedByMe = !it.likedByMe,
//                        likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
//                )
//            })
//            )
//            repository.likeById(id)
//        }
//    }

//    fun loadPosts() {
//        thread {
//            // Начинаем загрузку
//            _data.postValue(FeedModel(loading = true))
//            try {
//                // Данные успешно получены
//                val posts = repository.getAll()
//                FeedModel(posts = posts, empty = posts.isEmpty())
//            } catch (e: IOException) {
//                // Получена ошибка
//                FeedModel(error = true)
//            }.also(_data::postValue)
//        }
//    }
