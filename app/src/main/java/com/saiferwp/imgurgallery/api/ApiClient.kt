package com.saiferwp.imgurgallery.api

import com.saiferwp.imgurgallery.api.request.Request
import kotlinx.coroutines.Deferred
import retrofit2.Response

class ApiClient(
    private val api: Api
) {

    fun <T> executeAsync(request: Request<T>): Deferred<Response<T>> {
        return request.executeAsync(api)
    }
}