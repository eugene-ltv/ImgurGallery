package com.saiferwp.imgurgallery.wear

import android.graphics.Bitmap
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saiferwp.imgurgallery.data.model.GalleryImage

class GalleryAdapter(
    private val requestImage: (GalleryImage) -> (Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items = listOf<GalleryImage>()
    private var isLoaderVisible = false

    private val thumbnailCache = object : LruCache<String, Bitmap>(10) {
        override fun entryRemoved(
            evicted: Boolean,
            key: String?,
            oldValue: Bitmap?,
            newValue: Bitmap?
        ) {
            oldValue?.recycle()
        }
    }

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

    fun putThumbnailInCache(id: String, bitmap: Bitmap) {
        thumbnailCache.put(id, bitmap)
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
            (holder as RepoDataViewHolder).bind(items[position], thumbnailCache, requestImage)
        }
    }

    class RepoDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val description: TextView = itemView.findViewById(R.id.gallery_item_description)
        private val image: ImageView = itemView.findViewById(R.id.gallery_item_image)
        private val progress: ProgressBar = itemView.findViewById(R.id.gallery_item_progress)

        fun bind(
            galleryImage: GalleryImage,
            thumbnailCache: LruCache<String, Bitmap>,
            requestImage: (GalleryImage) -> (Unit)
        ) {
            description.text = galleryImage.title
            loadImage(galleryImage, thumbnailCache, requestImage)
        }

        private fun loadImage(
            galleryImage: GalleryImage,
            thumbnailCache: LruCache<String, Bitmap>,
            requestImage: (GalleryImage) -> Unit
        ) {
            image.setImageBitmap(null)
            val thumbnail = thumbnailCache.get(galleryImage.id)
            if (thumbnail != null) {
                image.setImageBitmap(thumbnail)
                progress.visibility = View.GONE
            } else {
                progress.visibility = View.VISIBLE
                requestImage(galleryImage)
            }
        }
    }

    class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}