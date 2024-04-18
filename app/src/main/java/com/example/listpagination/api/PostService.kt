package com.example.listpagination.api

import android.content.Context
import com.example.listpagination.api.interceptors.NoInternetInterceptor
import com.example.listpagination.model.Post
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface PostService {
    @GET("posts")
    suspend fun getPosts(@Query("_page") page: Int): Response<List<Post>>

    companion object {

        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

        fun create(context : Context): PostService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .addInterceptor(NoInternetInterceptor(context = context))
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PostService::class.java)
        }
    }
}