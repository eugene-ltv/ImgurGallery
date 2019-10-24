package com.saiferwp.imgurgallery.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.api.model.GalleryItem

class GalleryListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = mutableListOf<GalleryItem>()
    private var isLoaderVisible = false

    fun getItemsSize() = items.size

    fun addData(
        items: List<GalleryItem>
    ) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun showLoading(show : Boolean) {
        isLoaderVisible = show
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LOADING -> ProgressHolder(
                inflater.inflate(R.layout.list_item_loading, parent, false)
            )
            else -> RepoDataViewHolder(
                inflater.inflate(R.layout.list_item_gallery_item, parent, false)
            )
        }
    }

    override fun getItemCount() = items.size + if (isLoaderVisible) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == items.size) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!isLoaderVisible || (isLoaderVisible && position < items.size)) {
            (holder as RepoDataViewHolder).bind(items[position])
        }
    }

    class RepoDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name: TextView = itemView.findViewById(R.id.textView_gallery_item_name)
        private val description: TextView = itemView.findViewById(R.id.textView_gallery_item_description)

        fun bind(
            repoData: GalleryItem
        ) {
            name.text = repoData.id
            description.text = repoData.title
        }
    }

    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}