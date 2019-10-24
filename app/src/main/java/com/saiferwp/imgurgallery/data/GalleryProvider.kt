package com.saiferwp.imgurgallery.data

import com.saiferwp.imgurgallery.api.ApiClient
import com.saiferwp.imgurgallery.api.request.GalleryRequest
import com.saiferwp.imgurgallery.api.response.GalleryResponse
import java.net.UnknownHostException

class GalleryProvider(
    private val apiClient: ApiClient
) {

    suspend fun getGallery(currentPage: Int): GalleryResponse? {
        try {
            val request = GalleryRequest(currentPage)
            val response = apiClient.executeAsync(request).await()

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()

                return body
            } else {
                // Error, empty result
            }
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                // No internet
            }
        }
        return null
    }
}