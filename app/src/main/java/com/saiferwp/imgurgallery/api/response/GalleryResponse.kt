package com.saiferwp.imgurgallery.api.response

import com.google.gson.annotations.SerializedName
import com.saiferwp.imgurgallery.api.model.GalleryItem

data class GalleryResponse(
    @SerializedName("data") val data: List<GalleryItem> = ArrayList()
) : Response()

