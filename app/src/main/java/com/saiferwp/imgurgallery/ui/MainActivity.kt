package com.saiferwp.imgurgallery.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.saiferwp.imgurgallery.R

class MainActivity : AppCompatActivity() {

    private lateinit var mainFragment: MainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mainFragment = MainFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return mainFragment.onSupportNavigateUp()
    }
}
