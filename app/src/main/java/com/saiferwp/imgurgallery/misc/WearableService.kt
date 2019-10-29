package com.saiferwp.imgurgallery.misc

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.wearable.*
import com.saiferwp.imgurgallery.App
import com.saiferwp.imgurgallery.WearConstants
import com.saiferwp.imgurgallery.data.model.GalleryImage
import com.saiferwp.imgurgallery.data.model.GallerySection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.ObjectOutput
import java.io.ObjectOutputStream
import java.util.*

class WearableService : WearableListenerService() {

    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path.startsWith(WearConstants.PATH_GALLERY_HOT)) {
            doGalleryRequest(messageEvent.path
                .removePrefix(WearConstants.PATH_GALLERY_HOT).toInt()
            )
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val item = event.dataItem
                if (item.uri.path!!.compareTo(WearConstants.IMAGE_REQUEST) == 0) {
                    onLoadImageRequest(item)
                }
            }
        }
    }

    private fun doGalleryRequest(page: Int) {
        coroutineScope.launch {
            val galleryProvider = App.component.getGalleryProvider()

            val gallery = galleryProvider.getGallery(GallerySection.HOT, page)
            if (gallery != null) {
                postGallery(gallery)
            }
        }
    }

    private fun postGallery(gallery: List<GalleryImage>) {
        val bos = ByteArrayOutputStream()
        var out: ObjectOutput? = null
        try {
            out = ObjectOutputStream(bos)
            for (image in gallery) {
                out.writeObject(image)
            }

            val putDataMapReq = PutDataMapRequest.create(WearConstants.PATH_GALLERY_HOT)
            val dataMap = putDataMapReq.dataMap
            dataMap.putLong("time", Date().time)
            dataMap.putAsset(
                WearConstants.ASSET_GALLERY,
                Asset.createFromBytes(bos.toByteArray())
            )
            val putDataReq = putDataMapReq.asPutDataRequest()
            putDataReq.setUrgent()

            Wearable.getDataClient(this)
                .putDataItem(putDataReq)

        } catch (ignore: IOException) {
        } finally {
            try {
                out?.close()
            } catch (ignore: IOException) {}
            try {
                bos.close()
            } catch (ignore: IOException) {}
        }
    }


    private fun onLoadImageRequest(item: DataItem) {
        val dataMap = DataMapItem.fromDataItem(item).dataMap
        val id = dataMap.getString(WearConstants.FIELD_ID)
        val link = dataMap.getString(WearConstants.FIELD_LINK)
        prepareAndPostBitmap(link, id)
    }

    private fun prepareAndPostBitmap(link: String, id: String) {
        coroutineScope.launch {
            val bitmapFuture = Glide.with(this@WearableService)
                .asBitmap()
                .load(link)
                .apply(
                    RequestOptions()
                        .override(300, 300)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                )
                .submit()

            val bitmap = try {
                bitmapFuture.get()
            } catch (ex: Exception) {
                return@launch
            }

            val putDataMap = PutDataMapRequest.create(WearConstants.IMAGE_RESPONSE)
            putDataMap.dataMap.putLong("time", Date().time)
            putDataMap.dataMap.putString(WearConstants.FIELD_ID, id)
            putDataMap.dataMap.putAsset(WearConstants.ASSET_BITMAP, toAsset(bitmap))
            val request = putDataMap.asPutDataRequest()
            request.setUrgent()

            Wearable.getDataClient(this@WearableService)
                .putDataItem(request)
        }
    }

    private fun toAsset(bitmap: Bitmap): Asset {
        var byteStream: ByteArrayOutputStream? = null
        try {
            byteStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream)
            return Asset.createFromBytes(byteStream.toByteArray())
        } finally {
            if (null != byteStream) {
                try {
                    byteStream.close()
                } catch (e: IOException) {
                    // ignore
                }
            }
        }
    }
}