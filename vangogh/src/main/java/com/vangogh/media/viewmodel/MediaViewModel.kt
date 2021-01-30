package com.vangogh.media.viewmodel

import android.app.Application
import android.database.ContentObserver
import android.provider.BaseColumns
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vangogh.media.config.VanGogh
import com.vangogh.media.extend.registerObserver
import com.vangogh.media.models.MediaDir
import com.vangogh.media.models.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @ClassName MediaViewModel
 * @Description MediaViewModel query condition
 * @Author dhl
 * @Date 2021/1/28 10:30
 * @Version 1.0
 */
class MediaViewModel(application: Application) : MediaBaseViewModel(application) {

    companion object {
        const val TAG = "MediaViewModel"
    }

    private val _lvMediaData = MutableLiveData<List<MediaItem>>()
    val lvMediaData: LiveData<List<MediaItem>>
        get() = _lvMediaData

    private val _lvMediaDirData = MutableLiveData<List<MediaDir>>()
    val lvMediaDirData: LiveData<List<MediaDir>>
        get() = _lvMediaDirData


    private val _lvDataChanged = MutableLiveData<Boolean>()

    val lvDataChanged: LiveData<Boolean>
        get() = _lvDataChanged

    private var contentObserver: ContentObserver? = null


    /**
     * 至少需要高宽，时间
     */
    private val mediaProjection = arrayOf( //查询图片需要的数据列
        BaseColumns._ID,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  //图片的显示名称  aaa.jpg
        MediaStore.Images.Media.SIZE,  //图片的大小，long型  132492
        MediaStore.Images.Media.WIDTH,  //图片的宽度，int型  1920
        MediaStore.Images.Media.HEIGHT,  //图片的高度，int型  1080
        MediaStore.Images.Media.MIME_TYPE,  //图片的类型     image/jpeg
        MediaStore.Images.Media.DURATION,
        MediaStore.Images.Media.DATE_ADDED
    )

    private fun registerContentObserver() {
        if (contentObserver == null) {
            contentObserver = getApplication<Application>().contentResolver.registerObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ) {
                _lvDataChanged.value = true
            }
        }
    }

    fun getMedia(bucketId: String? = null) {
        launchDataLoad {
            val medias = queryImages(bucketId)
            _lvMediaData.postValue(medias)
            val mediaDirList = getMediaDir(medias)
            _lvMediaDirData.postValue(mediaDirList)
            registerContentObserver()
        }
    }


    @WorkerThread
    suspend fun queryImages(bucketId: String?): MutableList<MediaItem> {
        var mediaItemList = mutableListOf<MediaItem>()
        withContext(Dispatchers.IO) {
            val uri = MediaStore.Files.getContentUri("external")
            val sortOrder = mediaProjection[mediaProjection.size - 1] + " DESC"

            val cursor = getApplication<Application>().contentResolver.query(
                uri,
                mediaProjection,
                VanGogh.selection,
                VanGogh.selectArgs,
                sortOrder
            )
            Log.e(TAG, VanGogh.selectArgs.contentToString())
            while (cursor!!.moveToNext()) {
                //查询数据
                val imageId: Long =
                    cursor.getLong(cursor.getColumnIndexOrThrow(mediaProjection[0]))
                val bucketId: Long =
                    cursor.getLong(cursor.getColumnIndexOrThrow(mediaProjection[1]))
                val imagePath: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(mediaProjection[2]))
                var bucketName: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(mediaProjection[3]))
                val imageSize: Long =
                    cursor.getLong(cursor.getColumnIndexOrThrow(mediaProjection[4]))
                val imageWidth: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(mediaProjection[5]))
                val imageHeight: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(mediaProjection[6]))
                val mediaMineType: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(mediaProjection[7]))
                val mediaDuration: Long =
                    cursor.getLong(cursor.getColumnIndexOrThrow(mediaProjection[8]))
                val mediaItem = MediaItem()
                mediaItem.id = imageId
                mediaItem.bucketId = bucketId
                mediaItem.bucketName = bucketName
                mediaItem.path = imagePath
                mediaItem.width = imageWidth
                mediaItem.height = imageHeight
                mediaItem.size = imageSize
                mediaItem.mineType = mediaMineType
                mediaItem.duration = mediaDuration
                Log.e(TAG, "path = $imagePath:::imageSize = $imageSize:::width = $imageWidth:: bucketName::=$bucketName")
                mediaItemList.add(mediaItem)
            }
        }
        return mediaItemList
    }

    /**
     *  MediaFolder
     */
    @WorkerThread
    private fun getMediaDir(mediaList: List<MediaItem>): MutableList<MediaDir> {
        var mediaDirList = mutableListOf<MediaDir>()
        mediaList.forEach {
            val mediaDir = MediaDir()
            mediaDir.id = it.id
            mediaDir.bucketId = it.bucketId
            mediaDir.name = it.bucketName
            if (!mediaDirList.contains(mediaDir)) {
                mediaDir.medias.add(it)
                mediaDirList.add(mediaDir)
            } else {
                mediaDirList[mediaDirList.indexOf(mediaDir)]
                    .medias.add(it)
            }
        }
        Log.e(TAG, mediaDirList.toString())
        return mediaDirList
    }

    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

}