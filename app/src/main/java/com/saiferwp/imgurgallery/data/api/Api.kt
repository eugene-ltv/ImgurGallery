package com.saiferwp.imgurgallery.data.api

import com.saiferwp.imgurgallery.data.api.response.GalleryResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("gallery/hot/{page}")
    fun getGalleryAsync(
        @Path("page") currentPage: Int,
        @Query("showViral") showViral: String = "true"
    ): Deferred<Response<GalleryResponse>>

}