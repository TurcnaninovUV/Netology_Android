package ru.netology.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import ru.netology.db.AppDb
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryImpl
import javax.inject.Inject
import javax.inject.Singleton

class RemovePostWorker(
    appllicationContext: Context,
    params: WorkerParameters,
    private val repository: PostRepository
) : CoroutineWorker(appllicationContext, params) {
    companion object {
        const val name = "ru.netology.work.RemovePostsWorker"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getLong(name, 0L)
        if (id == 0L) {
            return Result.failure()
        }
        return try {
            repository.removeById(id)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

@Singleton
class RemovePostsWorkerFactory @Inject constructor(
    private val repository: PostRepository,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        RemovePostWorker::class.java.name ->
            RemovePostWorker(appContext, workerParameters, repository)
        else ->
            null
    }
}