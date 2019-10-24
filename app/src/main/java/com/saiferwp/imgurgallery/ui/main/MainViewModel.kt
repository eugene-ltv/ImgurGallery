package com.saiferwp.imgurgallery.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saiferwp.imgurgallery.App
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    fun doRequest() {
        val reposProvider = App.component.getReposProvider()

        viewModelScope.launch {
            val galleryTop = reposProvider.getGallery(0)
            println(galleryTop)
        }
    }
}
