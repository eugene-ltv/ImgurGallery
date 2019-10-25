package com.saiferwp.imgurgallery.api.model

import com.google.gson.annotations.SerializedName

data class GalleryItem(
    val id: String,
    val title: String,
    val type: String?,
    @SerializedName("images") val images: List<GalleryImage> = ArrayList(),
    val width: Int,
    val height: Int,
    val mp4: String?
) {

    data class GalleryImage(
        val link: String,
        val width: Int,
        val height: Int
    )
}