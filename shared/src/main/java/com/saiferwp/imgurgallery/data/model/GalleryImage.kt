package com.saiferwp.imgurgallery.data.model

import java.io.Serializable

data class GalleryImage(
    val id: String,
    val title: String,
    val type: Type,
    val width: Int,
    val height: Int,
    val link: String?
) : Serializable {

    enum class Type {
        IMAGE,
        GIF,
        MP4
    }
}