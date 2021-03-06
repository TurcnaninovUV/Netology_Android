package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.auth.AppAuth
import ru.netology.auth.AuthState
import ru.netology.db.AppDb
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao(),
        AppDb.getInstance(context = application).postWorkDao()
    )

    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    fun authentication(login: String, pass: String) {
        viewModelScope.launch {
            repository.authentication(login, pass)
        }
    }

    fun registration(name: String, login: String, pass: String) {
        viewModelScope.launch {
            repository.registration(name, login, pass)
        }
    }
}