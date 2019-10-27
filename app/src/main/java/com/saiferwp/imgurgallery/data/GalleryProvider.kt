package com.saiferwp.imgurgallery.data

import com.saiferwp.imgurgallery.api.ApiClient
import com.saiferwp.imgurgallery.api.request.GalleryHotRequest
import com.saiferwp.imgurgallery.api.request.GalleryTopRequest
import com.saiferwp.imgurgallery.api.request.GalleryUserSubmittedRequest
import com.saiferwp.imgurgallery.data.model.GalleryImage
import com.saiferwp.imgurgallery.data.model.GalleryImageMapper
import com.saiferwp.imgurgallery.data.model.GallerySection
import java.net.UnknownHostException

class GalleryProvider(
    private val apiClient: ApiClient
) {

    suspend fun getGallery(
        gallerySection: GallerySection,
        currentPage: Int
    ): List<GalleryImage>? {
        try {
            val request = when (gallerySection) {
                GallerySection.HOT -> GalleryHotRequest(currentPage)
                GallerySection.USER_SUBMITTED -> GalleryUserSubmittedRequest(currentPage)
                GallerySection.TOP -> GalleryTopRequest(currentPage)
            }

            val response = apiClient.executeAsync(request).await()

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()

                return if (body != null && body.data.isNotEmpty()) {
                    body.data.map { GalleryImageMapper.map(it) }
                } else {
                    emptyList()
                }
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