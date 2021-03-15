package ru.netology.viewmodel

import androidx.lifecycle.ViewModel
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.get()
    fun like() = repository.like()
    fun share() = repository.repost()
    fun reductionLike() = data.value?.let { repository.reduction(it.likes) }
    fun reductionRepost() = data.value?.let { repository.reduction(it.repost) }
}