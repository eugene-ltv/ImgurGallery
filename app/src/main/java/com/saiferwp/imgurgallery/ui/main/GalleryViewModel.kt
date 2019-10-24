package com.saiferwp.imgurgallery.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saiferwp.imgurgallery.App
import com.saiferwp.imgurgallery.api.model.GalleryItem
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private val galleryLiveData = MutableLiveData<List<GalleryItem>>()

    private var currentPage = 0
    internal var isLastPage = false
    internal var isLoading = false

    private fun doRequest() {
        isLoading = true
        viewModelScope.launch {
            val provider = App.component.getReposProvider()
            val reposList = provider.getGallery(currentPage)
            if (reposList != null) {
                if (reposList.data.isEmpty()) {
                    isLastPage = true
                }

                isLoading = false
                galleryLiveData.value = reposList.data
            }
        }
    }

    fun loadGallery(): MutableLiveData<List<GalleryItem>> {
        doRequest()
        return galleryLiveData
    }

    fun loadMoreItems() {
        currentPage++
        doRequest()
    }
}
