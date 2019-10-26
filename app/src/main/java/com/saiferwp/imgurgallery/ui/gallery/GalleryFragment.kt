package com.saiferwp.imgurgallery.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.data.model.GallerySection
import com.saiferwp.imgurgallery.misc.PaginationListener

class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val adapter: GalleryAdapter =
        GalleryAdapter()

    private lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerView_repos_list)

//        val layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position >= adapter.getItemsSize()) 2 else 1
            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object :
            PaginationListener(
                layoutManager
            ) {
            override fun loadMoreItems() {
                viewModel.loadMoreItems()
            }

            override fun isLastPage(): Boolean {
                return viewModel.isLastPage
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        viewModel.gallerySection = arguments?.getSerializable("gallerySection") as GallerySection
        viewModel.loadGallery()
            .observe(this, Observer { list ->
                adapter.addData(list)

                adapter.showLoading(!viewModel.isLastPage)
            })
    }
}
