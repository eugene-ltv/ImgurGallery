package com.saiferwp.imgurgallery.api

import com.saiferwp.imgurgallery.api.response.GalleryResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @GET("gallery/hot/{page}")
    fun getGalleryHotAsync(
        @Path("page") currentPage: Int
    ): Deferred<Response<GalleryResponse>>

    @GET("gallery/top/{page}")
    fun getGalleryTopAsync(
        @Path("page") currentPage: Int
    ): Deferred<Response<GalleryResponse>>

    @GET("gallery/user/{page}")
    fun getGalleryUserSubmittedAsync(
        @Path("page") currentPage: Int,
        @Query("showViral") showViral: String = "true"
    ): Deferred<Response<GalleryResponse>>

}