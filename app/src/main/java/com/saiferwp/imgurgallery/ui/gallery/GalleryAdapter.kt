package com.saiferwp.imgurgallery.ui.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.saiferwp.imgurgallery.R
import com.saiferwp.imgurgallery.data.model.GalleryImage
import com.saiferwp.imgurgallery.misc.GlideSimpleRequestListener

class GalleryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<GalleryImage>()
    private var isLoaderVisible = false
    internal var dynamicImageSizeRatio = false

    fun getItemsSize() = items.size

    fun setData(
        items: List<GalleryImage>
    ) {
        this.items = items
        notifyDataSetChanged()
    }

    fun showLoading(show: Boolean) {
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

    override fun getItemCount() = items.size +
            if (isLoaderVisible && items.isNotEmpty()) 1 else 0

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == items.size) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (!isLoaderVisible || (isLoaderVisible && position < items.size)) {
            (holder as RepoDataViewHolder).bind(items[position], dynamicImageSizeRatio)
        }
    }

    class RepoDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val description: TextView = itemView.findViewById(R.id.gallery_item_description)
        private val image: ImageView = itemView.findViewById(R.id.gallery_item_image)
        private val progress: ProgressBar = itemView.findViewById(R.id.gallery_item_progress)

        fun bind(
            galleryImage: GalleryImage,
            dynamicImageSizeRatio: Boolean
        ) {
            description.text = galleryImage.title

            val layoutParams = image.layoutParams as ConstraintLayout.LayoutParams
            if (dynamicImageSizeRatio) {
                val imageRatio = galleryImage.height.toFloat() / galleryImage.width
                layoutParams.dimensionRatio = "1:${if (imageRatio > 2) 2f else imageRatio}"
            } else {
                layoutParams.dimensionRatio = "1:1"
            }

            loadImage(galleryImage)
        }

        private fun loadImage(galleryImage: GalleryImage) {
            Glide.with(itemView.context)
                .clear(image)
            progress.visibility = View.VISIBLE

            Glide.with(itemView.context)
                .also {
                    if (galleryImage.type == GalleryImage.Type.GIF) {
                        it.asGif()
                    } else {
                        it.asBitmap()
                    }
                }
                .load(galleryImage.link)
                .apply(RequestOptions()
                    .override(galleryImage.width, galleryImage.height)
                    .also {
                        if (galleryImage.type == GalleryImage.Type.GIF ||
                            galleryImage.type == GalleryImage.Type.MP4
                        ) {
                            it.diskCacheStrategy(DiskCacheStrategy.DATA)
                        } else {
                            it.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        }
                    }
                )
                .listener(GlideSimpleRequestListener {
                    progress.visibility = View.GONE
                })
                .into(image)
        }
    }

    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}