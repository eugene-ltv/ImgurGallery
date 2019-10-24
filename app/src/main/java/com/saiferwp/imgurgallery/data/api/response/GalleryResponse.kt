package com.saiferwp.imgurgallery.data.api.response

import com.google.gson.annotations.SerializedName
import com.saiferwp.imgurgallery.data.api.model.GalleryItem

data class GalleryResponse(
    @SerializedName("data")
    private val data: List<GalleryItem> = ArrayList()
) : Response()

