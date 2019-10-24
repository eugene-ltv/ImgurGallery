package com.saiferwp.imgurgallery.data.api.request

import com.saiferwp.imgurgallery.data.api.Api
import com.saiferwp.imgurgallery.data.api.response.GalleryResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response

class GalleryRequest(private val currentPage: Int) : Request<GalleryResponse>() {
    override fun executeAsync(api: Api): Deferred<Response<GalleryResponse>> {
        return api.getGalleryAsync(currentPage)
    }
}