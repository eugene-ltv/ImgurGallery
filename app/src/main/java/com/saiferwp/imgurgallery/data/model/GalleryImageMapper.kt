package com.saiferwp.imgurgallery.data.model

import com.saiferwp.imgurgallery.api.model.GalleryItem

object GalleryImageMapper {

    fun map(galleryItem: GalleryItem): GalleryImage {

        var type = "image/jpeg"
        var link = ""
        val width: Int
        val height: Int

        if (galleryItem.images.isNullOrEmpty()) {
            type = galleryItem.type ?: type

            width = galleryItem.width
            height = galleryItem.height

            if (!galleryItem.mp4.isNullOrEmpty()) {
                type = "video/mp4"
                link = galleryItem.mp4
            }

        } else {
            val galleryImage = galleryItem.images[0]
            type = galleryImage.type ?: type
            link = galleryImage.link

            width = galleryImage.width
            height = galleryImage.height
        }

        return GalleryImage(
            id = galleryItem.id,
            title = galleryItem.title,
            type = when (type) {
                "video/mp4" -> GalleryImage.Type.MP4
                "image/gif" -> GalleryImage.Type.GIF
                else -> GalleryImage.Type.IMAGE
            },
            link = link,
            width = width,
            height = height
        )
    }
}