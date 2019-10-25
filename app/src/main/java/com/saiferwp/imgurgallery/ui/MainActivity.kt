package com.saiferwp.imgurgallery.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.ui.main.GalleryFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, GalleryFragment.newInstance())
                .commitNow()
        }
    }

}
