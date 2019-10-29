package com.saiferwp.imgurgallery.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.saiferwp.imgurgallery.App
import com.saiferwp.imgurgallery.R

class GalleryUserSubmittedFragment : GalleryFragment() {

    private lateinit var switchViral: Switch

    private val preferencesManager = App.component.getPreferencesManager()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.gallery_user_submitted_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchViral = view.findViewById(R.id.switch_viral)

        switchViral.isChecked = preferencesManager.getUserSubmittedShowViral()

        switchViral.setOnCheckedChangeListener { _, isChecked ->
            preferencesManager.setUserSubmittedShowViral(isChecked)
            viewModel.reload()
        }
    }
}
