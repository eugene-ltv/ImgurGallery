package com.saiferwp.imgurgallery.api.request

import com.saiferwp.imgurgallery.api.Api
import com.saiferwp.imgurgallery.api.response.GalleryResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

class GalleryRequest(private val currentPage: Int) : Request<GalleryResponse>() {
    override fun executeAsync(api: Api): Deferred<Response<GalleryResponse>> {
        return api.getGalleryAsync(currentPage)
    }
}