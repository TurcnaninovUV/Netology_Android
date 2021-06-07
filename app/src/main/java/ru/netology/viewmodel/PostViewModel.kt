package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.dao.PostDao
import ru.netology.db.AppDb
import ru.netology.dto.Post
import ru.netology.model.FeedModel
import ru.netology.model.FeedModelState
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import ru.netology.util.SingleLiveEvent


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    published = ""
)


class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())

    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated


    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAll()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun save() {
        edited.value?.let {
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.save(it)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = empty
    }

    fun removeById(id: Long) = viewModelScope.launch {
        try {
            repository.removeById(id)
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
        refreshPosts()
    }

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

    fun likeById(id: Long) {
        if (data.value?.posts.orEmpty().filter { it.id == id }.none { it.likedByMe }) {
            viewModelScope.launch {
                try {
                    repository.likeById(id)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        } else {
            viewModelScope.launch {
                try {
                    repository.dislikeById(id)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
    }

    fun repostById(id: Long) {
        TODO()
    }

}

//    private val repository: PostRepository = PostRepositoryImpl()
//    private val _data = MutableLiveData(FeedModel())
//    val data: LiveData<FeedModel>
//        get() = _data
//    val edited = MutableLiveData(empty)
//    private val _postCreated = SingleLiveEvent<Unit>()
//    val postCreated: LiveData<Unit>
//        get() = _postCreated
//    private val _networkError = SingleLiveEvent<String>()
//    val networkError: LiveData<String>
//        get() = _networkError
//
//    init {
//        loadPosts()
//    }
//
//    fun loadPosts() {
//        _data.value = FeedModel(loading = true)
//        repository.getAllAsync(object : PostRepository.Callback<List<Post>> {
//            override fun onSuccess(posts: List<Post>) {
//                _data.value = FeedModel(posts = posts, empty = posts.isEmpty())
//            }
//
//            override fun onError(e: Exception) {
//                _data.value = FeedModel(error = true)
//            }
//        })
//    }
//
//    fun save() {
//        edited.value?.let {
//            repository.save(it, object : PostRepository.Callback<Post> {
//                override fun onSuccess(posts: Post) {
//                    loadPosts()
//                    _postCreated.value = Unit
//                }
//
//                override fun onError(e: Exception) {
//                    _networkError.value = e.message
//                }
//            })
//        }
//        edited.value = empty
//    }
//
//    fun edit(post: Post) {
//        edited.value = post
//    }
//
//    fun changeContent(content: String) {
//        val text = content.trim()
//        if (edited.value?.content == text) {
//            return
//        }
//        edited.value = edited.value?.copy(content = text)
//    }
//
//    fun likeById(id: Long) {
//        if (_data.value?.posts.orEmpty().filter { it.id == id }.none { it.likedByMe }) {
//            repository.likeById(id, object : PostRepository.Callback<Post> {
//                override fun onSuccess(value: Post) {
//                    _data.postValue(
//                        FeedModel(
//                            posts = _data.value?.posts.orEmpty()
//                                .map { if (it.id == value.id) value else it })
//                    )
//                }
//            })
//        } else repository.dislikeById(id, object : PostRepository.Callback<Post> {
//            override fun onSuccess(value: Post) {
//                _data.postValue(
//                    FeedModel(
//                        posts = _data.value?.posts.orEmpty()
//                            .map { if (it.id == value.id) value else it })
//                )
//            }
//
//            override fun onError(e: Exception) {
//                _networkError.value = e.message
//            }
//        })
//    }
//
//
//    fun removeById(id: Long) {
//        repository.removeById(id, object : PostRepository.Callback<Unit> {
//            override fun onSuccess(value: Unit) {
//                val posts = _data.value?.posts.orEmpty()
//                    .filter { it.id != id }
//                _data.postValue(
//                    _data.value?.copy(posts = posts, empty = posts.isEmpty())
//                )
//                loadPosts()
//            }
//
//            override fun onError(e: Exception) {
//                _networkError.value = e.message
//            }
//
//        })
//    }
//
//    fun repostById(id: Long) = repository.repostById(id)
//
//}



