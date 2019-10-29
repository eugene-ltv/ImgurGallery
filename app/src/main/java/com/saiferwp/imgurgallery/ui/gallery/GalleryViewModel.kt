package com.saiferwp.imgurgallery.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saiferwp.imgurgallery.App
import com.saiferwp.imgurgallery.data.model.GalleryImage
import com.saiferwp.imgurgallery.data.model.GallerySection
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    lateinit var gallerySection: GallerySection
    internal val galleryLiveData = MutableLiveData<List<GalleryImage>>(listOf())
    internal val initialLoadingLiveData = MutableLiveData<Boolean>(false)

    private var currentPage = 0
    internal var isLastPage = false
    internal var isLoading = false

    private val galleryProvider = App.component.getGalleryProvider()
    private val preferencesManager = App.component.getPreferencesManager()

    internal fun doRequest() {
        isLoading = true
        if (currentPage == 0) {
            initialLoadingLiveData.value = true
        }
        viewModelScope.launch {
            val gallery = galleryProvider.getGallery(gallerySection, currentPage)
            if (gallery != null) {
                if (gallery.isEmpty()) {
                    isLastPage = true
                }

                isLoading = false
                if (currentPage == 0) {
                    initialLoadingLiveData.value = false
                }

                galleryLiveData.value =
                    galleryLiveData.value?.plus(gallery)
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

    fun reload() {
        currentPage = 0
        isLoading = false
        isLastPage = false
        galleryLiveData.value = emptyList()
        doRequest()
    }
}

enum class LayoutType {
    GRID,
    LINEAR,
    STAGGERED_GRID
}
