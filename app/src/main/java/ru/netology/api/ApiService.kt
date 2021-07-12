    package ru.netology.api

import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.BuildConfig
import ru.netology.auth.AppAuth
import ru.netology.auth.AuthState
import ru.netology.dto.Media
import ru.netology.dto.Post
import ru.netology.dto.PushToken

    private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

    fun okhttp(vararg interceptors: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .apply {
            interceptors.forEach {
                this.addInterceptor(it)
            }
        }
        .build()

    fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(client)
        .build()

interface ApiService {
    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @POST("posts/{id}/repost")
    suspend fun repostById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun dislikeById(@Path("id") id: Long): Response<Post>

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(
        @Field("login") login: String,
        @Field("pass") pass: String): Response<AuthState>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registrationUser(
        @Field("name") name: String,
        @Field("login") login: String,
        @Field("pass") pass: String): Response<AuthState>

    @POST("users/push-tokens")
    suspend fun save(@Body pushToken: PushToken): Response<Unit>
}