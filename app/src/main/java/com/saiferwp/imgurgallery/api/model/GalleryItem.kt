package com.saiferwp.imgurgallery.api.model

data class GalleryItem(
    val id: String,
    val title: String,
    val type: String?,
    val images: List<Image> = ArrayList(),
    val width: Int,
    val height: Int,
    val mp4: String?
) {

    data class Image(
        val link: String,
        val width: Int,
        val height: Int,
        val type: String?
    )
}