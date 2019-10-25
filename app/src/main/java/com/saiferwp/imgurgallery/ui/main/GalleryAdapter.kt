package com.saiferwp.imgurgallery.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.api.model.GalleryItem

class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        private val description: TextView = itemView.findViewById(R.id.textView_gallery_item_description)
        private val image: ImageView = itemView.findViewById(R.id.textView_gallery_item_image)

        fun bind(
            galleryItem: GalleryItem
        ) {
            description.text = galleryItem.title

            Glide.with(itemView.context)
                .clear(image)

//            val layoutParams = image.layoutParams as ConstraintLayout.LayoutParams
            if (galleryItem.images.isNullOrEmpty()) {
//                layoutParams.dimensionRatio = "1:1"

                if (!galleryItem.mp4.isNullOrEmpty()) {
                    val imageRatio = galleryItem.height.toFloat() / galleryItem.width
//                    layoutParams.dimensionRatio = "1:${imageRatio}"

                    Glide.with(itemView.context)
                        .asBitmap()
                        .load(galleryItem.mp4)
                        .apply(RequestOptions()
                            .override(galleryItem.width, galleryItem.height)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC))
                        .into(image)
                }

            } else {
                val galleryImage = galleryItem.images[0]
//                val imageRatio = galleryImage.height.toFloat() / galleryImage.width
//                layoutParams.dimensionRatio = "1:${if (imageRatio > 2) 2f else imageRatio}"

                Glide.with(itemView.context)
                    .also {
                        if (galleryItem.type == "image/gif") {
                            it.asGif()
                        } else {
                            it.asBitmap()
                        }
                    }
                    .load(galleryImage.link)
                    .apply(RequestOptions()
                        .override(galleryImage.width, galleryImage.height)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .into(image)
            }
        }
    }

    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}