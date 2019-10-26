package com.saiferwp.imgurgallery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.misc.setupWithNavController


class MainFragment : Fragment() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = view?.findViewById<BottomNavigationView>(R.id.bottom_nav)

        val navGraphIds = listOf(R.navigation.hot, R.navigation.user, R.navigation.top)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = childFragmentManager,
            containerId = R.id.nav_host_container,
            intent = requireActivity().intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller?.observe(this, Observer { navController ->
            setupActionBarWithNavController(requireActivity() as AppCompatActivity, navController)
        })
        currentNavController = controller
    }

    fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}