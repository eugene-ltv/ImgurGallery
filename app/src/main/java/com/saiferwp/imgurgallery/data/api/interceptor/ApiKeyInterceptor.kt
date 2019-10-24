package com.saiferwp.imgurgallery.data.api.interceptor

import com.saiferwp.imgurgallery.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val newRequest =
            request.newBuilder()
                .addHeader("Authorization", "Client-ID " + BuildConfig.ImgurApiKey)
                .build()

        return chain.proceed(newRequest)
    }
}