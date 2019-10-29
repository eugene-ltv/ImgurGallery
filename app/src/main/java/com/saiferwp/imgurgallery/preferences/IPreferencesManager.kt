package com.saiferwp.imgurgallery.preferences

import com.saiferwp.imgurgallery.ui.gallery.LayoutType


interface IPreferencesManager {

    fun getGalleryLayoutType(): LayoutType
    fun setGalleryLayoutType(galleryLayoutType: LayoutType)

    fun getUserSubmittedShowViral(): Boolean
    fun setUserSubmittedShowViral(showViral: Boolean)
}
