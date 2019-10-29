package com.saiferwp.imgurgallery.wear

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import androidx.annotation.WorkerThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.*
import com.saiferwp.imgurgallery.WearConstants
import com.saiferwp.imgurgallery.data.model.GalleryImage
import com.saiferwp.imgurgallery.misc.PaginationListener
import kotlinx.coroutines.*
import java.io.BufferedInputStream
import java.io.IOException
import java.io.ObjectInput
import java.io.ObjectInputStream
import java.util.*

class GalleryActivity : WearableActivity(),
    DataClient.OnDataChangedListener {

    private lateinit var recyclerView: WearableRecyclerView
    private var adapter: GalleryAdapter = GalleryAdapter(::requestImage)

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private var currentPage = 0
    internal var isLastPage = false
    internal var isLoading = false

    private val gallery = mutableListOf<GalleryImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAmbientEnabled()

        initRecycler()
        doGalleryRequest()
    }

    private fun initRecycler() {
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object :
            PaginationListener(
                layoutManager
            ) {
            override fun loadMoreItems() {
                loadNextPage()
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    override fun onResume() {
        super.onResume()
        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()
        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun sendRequestGalleryMessage(node: String) {
        Wearable.getMessageClient(this).sendMessage(
            node,
            WearConstants.PATH_GALLERY_HOT + "$currentPage",
            ByteArray(0)
        )
    }

    fun loadNextPage() {
        currentPage++
        doGalleryRequest()
    }

    private fun doGalleryRequest() {
        isLoading = true
        coroutineScope.launch {
            val nodes = getNodes()
            for (node in nodes) {
                sendRequestGalleryMessage(node)
            }
        }
    }

    @WorkerThread
    private fun getNodes(): Collection<String> {
        val results = HashSet<String>()
        val nodeListTask = Wearable.getNodeClient(applicationContext).connectedNodes
        try {
            val nodes = Tasks.await(nodeListTask)
            for (node in nodes) {
                results.add(node.id)
            }
        } catch (ignore: Exception) {
        }
        return results
    }

    private fun requestImage(galleryImage: GalleryImage) {
        val dataMap = PutDataMapRequest.create(WearConstants.IMAGE_REQUEST)
        dataMap.dataMap.putLong("time", Date().time)
        dataMap.dataMap.putString(WearConstants.FIELD_ID, galleryImage.id)
        dataMap.dataMap.putString(WearConstants.FIELD_LINK, galleryImage.link)
        val request = dataMap.asPutDataRequest()
        request.setUrgent()

        Wearable.getDataClient(this).putDataItem(request)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem
                if (item.uri.path!!.compareTo(WearConstants.PATH_GALLERY_HOT) == 0) {
                    fetchGalleryFromDataEvent(item)

                } else if (item.uri.path!!.compareTo(WearConstants.IMAGE_RESPONSE) == 0) {
                    fetchBitmapFromDataEvent(item)
                }
            }
        }
    }

    private fun fetchGalleryFromDataEvent(item: DataItem) {
        val dataMap = DataMapItem.fromDataItem(item).dataMap
        val dataAsset = dataMap.getAsset(WearConstants.ASSET_GALLERY)
        coroutineScope.launch {
            loadGalleryFromAsset(dataAsset)
        }
    }

    private fun fetchBitmapFromDataEvent(item: DataItem) {
        val dataMap = DataMapItem.fromDataItem(item).dataMap
        val id = dataMap.getString(WearConstants.FIELD_ID)
        val bitmapAsset = dataMap.getAsset(WearConstants.ASSET_BITMAP)

        coroutineScope.launch {
            val bitmap = loadBitmapFromAsset(bitmapAsset)
            if (bitmap != null) {
                launch(Dispatchers.Main) {
                    adapter.putThumbnailInCache(id, bitmap)
                }
            }
        }
    }

    private fun loadGalleryFromAsset(dataAsset: Asset) {
        val gallery = mutableListOf<GalleryImage>()

        val getFdForAssetResponseTask = Wearable.getDataClient(
            applicationContext
        ).getFdForAsset(dataAsset)

        val getFdForAssetResponse =
            Tasks.await<DataClient.GetFdForAssetResponse>(getFdForAssetResponseTask)

        val assetInputStream = getFdForAssetResponse.inputStream

        var input: ObjectInput? = null
        try {
            input = ObjectInputStream(BufferedInputStream(assetInputStream))
            var galleryItem: GalleryImage? =
                input.readObject() as GalleryImage
            while (galleryItem != null) {
                gallery.add(galleryItem)
                galleryItem = input.readObject() as GalleryImage
            }
        } catch (ignore: Exception) {
        } finally {
            try {
                input?.close()
            } catch (ignore: IOException) {
            }

            onNewGalleryData(gallery)
        }
    }

    private fun onNewGalleryData(newData: MutableList<GalleryImage>) {
        isLoading = false
        if (newData.isEmpty()) {
            isLastPage = true
        }

        coroutineScope.launch(Dispatchers.Main) {
            gallery.addAll(newData)
            adapter.setData(gallery)
            adapter.showLoading(newData.isNotEmpty())
        }
    }

    private fun loadBitmapFromAsset(asset: Asset): Bitmap? {
        val getFdForAssetResponseTask =
            Wearable.getDataClient(this@GalleryActivity).getFdForAsset(asset)

        return try {
            val getFdForAssetResponse =
                Tasks.await<DataClient.GetFdForAssetResponse>(getFdForAssetResponseTask)

            val assetInputStream = getFdForAssetResponse.inputStream

            if (assetInputStream != null) {
                BitmapFactory.decodeStream(assetInputStream)
            } else {
                null
            }

        } catch (exception: Exception) {
            null
        }
    }
}
