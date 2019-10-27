package com.saiferwp.imgurgallery.data.model

data class GalleryImage(
    val title: String,
    val type: Type,
    val width: Int,
    val height: Int,
    val link: String?
) {

    enum class Type{
        IMAGE,
        GIF,
        MP4
    }
}