package com.saiferwp.imgurgallery.preferences

import android.content.SharedPreferences
import com.saiferwp.imgurgallery.Config
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

    override fun getUserSubmittedShowViral(): Boolean {
        return sharedPreferences.getBoolean(
            Key.KEY_USER_SUBMITTED_SHOW_VIRAL.name, Config.SHOW_VIRAL_DEFAULT
        )
    }

    override fun setUserSubmittedShowViral(showViral: Boolean) {
        sharedPreferences.edit().putBoolean(
            Key.KEY_USER_SUBMITTED_SHOW_VIRAL.name,
            showViral
        ).apply()
    }

    private enum class Key {
        KEY_GALLERY_LAYOUT_TYPE,
        KEY_USER_SUBMITTED_SHOW_VIRAL
    }
}
