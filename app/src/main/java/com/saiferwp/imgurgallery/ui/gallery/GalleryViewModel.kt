package com.saiferwp.imgurgallery.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saiferwp.imgurgallery.App
import com.saiferwp.imgurgallery.api.model.GalleryItem
import com.saiferwp.imgurgallery.data.model.GallerySection
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    lateinit var gallerySection: GallerySection
    internal val galleryLiveData = MutableLiveData<List<GalleryItem>>(listOf())

    private var currentPage = 0
    internal var isLastPage = false
    internal var isLoading = false

    private val galleryProvider = App.component.getGalleryProvider()
    private val preferencesManager = App.component.getPreferencesManager()

    internal fun doRequest() {
        isLoading = true
        viewModelScope.launch {

            val reposList = galleryProvider.getGallery(gallerySection, currentPage)
            if (reposList != null) {
                if (reposList.data.isEmpty()) {
                    isLastPage = true
                }

                isLoading = false
                galleryLiveData.value =
                    galleryLiveData.value?.plus(reposList.data)
            }
        }
    }

    fun loadMoreItems() {
        currentPage++
        doRequest()
    }

    fun getLayoutType(): LayoutType {
        return preferencesManager.getGalleryLayoutType()
    }

    fun setLayoutType(layoutType: LayoutType) {
        preferencesManager.setGalleryLayoutType(layoutType)
    }
}

enum class LayoutType {
    GRID,
    LINEAR,
    STAGGERED_GRID
}
