package com.saiferwp.imgurgallery.ui.gallery

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.data.model.GallerySection
import com.saiferwp.imgurgallery.misc.PaginationListener

open class GalleryFragment : Fragment() {

    private lateinit var progress: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private val adapter: GalleryAdapter =
        GalleryAdapter()

    lateinit var viewModel: GalleryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this).get(GalleryViewModel::class.java)

        viewModel.gallerySection = arguments?.getSerializable("gallerySection") as GallerySection

        viewModel.galleryLiveData
            .observe(this, Observer { list ->
                adapter.setData(list)

                adapter.showLoading(!viewModel.isLastPage)
            })
        viewModel.doRequest()

        viewModel.initialLoadingLiveData
            .observe(this, Observer { isLoading ->
                progress.visibility = if (isLoading) View.VISIBLE else View.GONE
            })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress = view.findViewById(R.id.gallery_progress)
        recyclerView = view.findViewById(R.id.recyclerView_repos_list)
        recyclerView.adapter = adapter
        configureRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.gallery_menu, menu)
        when (viewModel.getLayoutType()) {
            LayoutType.GRID -> {
                menu.findItem(R.id.grid_view).isVisible = false
            }
            LayoutType.LINEAR -> {
                menu.findItem(R.id.linear_view).isVisible = false
            }
            LayoutType.STAGGERED_GRID -> {
                menu.findItem(R.id.staggered_grid_view).isVisible = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = true
        (when (item.itemId) {
            R.id.linear_view -> viewModel.setLayoutType(LayoutType.LINEAR)
            R.id.grid_view -> viewModel.setLayoutType(LayoutType.GRID)
            R.id.staggered_grid_view -> viewModel.setLayoutType(LayoutType.STAGGERED_GRID)
            else ->
                result = super.onOptionsItemSelected(item)
        })
        configureRecyclerView()
        activity?.invalidateOptionsMenu()
        return result
    }

    private fun configureRecyclerView() {
        adapter.dynamicImageSizeRatio =
            viewModel.getLayoutType() == LayoutType.STAGGERED_GRID ||
                    viewModel.getLayoutType() == LayoutType.LINEAR

        val layoutManager: RecyclerView.LayoutManager =
            when (viewModel.getLayoutType()) {
                LayoutType.GRID -> {
                    GridLayoutManager(requireContext(), 2)
                        .apply {
                            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return if (position >= adapter.getItemsSize()) 2 else 1
                                }
                            }
                        }
                }
                LayoutType.LINEAR -> {
                    LinearLayoutManager(requireContext())
                }
                LayoutType.STAGGERED_GRID -> {
                    StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
                }
            }
        recyclerView.layoutManager = layoutManager

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
}
