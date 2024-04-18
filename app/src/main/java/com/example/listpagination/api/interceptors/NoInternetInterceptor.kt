package com.example.listpagination.api.interceptors

import android.content.Context
import com.example.listpagination.ListApp
import com.example.listpagination.api.exceptions.NoNetworkException
import com.example.listpagination.utils.isNetworkAvailable
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NoInternetInterceptor(private val context : Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable(context)) {
            throw NoNetworkException()
        }
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }
}