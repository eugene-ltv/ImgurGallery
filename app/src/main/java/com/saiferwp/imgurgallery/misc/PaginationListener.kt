package com.saiferwp.imgurgallery.misc

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

abstract class PaginationListener(
    private val layoutManager: RecyclerView.LayoutManager
) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val firstVisibleItemPosition = if(layoutManager is StaggeredGridLayoutManager) {
            val arr = IntArray(2)
            layoutManager.findFirstCompletelyVisibleItemPositions(arr)
            arr[0]
        } else {
            (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        }


        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
            ) {
                loadMoreItems()
            }
        }
    }

    abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}