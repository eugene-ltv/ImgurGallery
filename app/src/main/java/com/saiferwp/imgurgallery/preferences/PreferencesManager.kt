package com.saiferwp.imgurgallery.preferences

import android.content.SharedPreferences
import com.saiferwp.imgurgallery.ui.gallery.LayoutType

class PreferencesManager(
        private val sharedPreferences: SharedPreferences
) : IPreferencesManager {

    override fun getGalleryLayoutType(): LayoutType {
        return LayoutType.valueOf(
            sharedPreferences.getString(
                Key.KEY_GALLERY_LAYOUT_TYPE.name,
                LayoutType.GRID.name
            )!!
        )
    }

    override fun setGalleryLayoutType(galleryLayoutType: LayoutType) {
        sharedPreferences.edit().putString(
            Key.KEY_GALLERY_LAYOUT_TYPE.name,
            galleryLayoutType.name
        ).apply()
    }

    private enum class Key {
        KEY_GALLERY_LAYOUT_TYPE
    }
}
