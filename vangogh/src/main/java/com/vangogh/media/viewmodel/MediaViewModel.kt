package com.vangogh.media.viewmodel

import android.app.Application
import android.content.ContentUris
import android.database.ContentObserver
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.log.EasyLog
import com.media.vangogh.R
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.extend.registerObserver
import com.vangogh.media.models.MediaDir
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.ImageUtils
import com.vangogh.media.utils.SelectedMediaManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @ClassName MediaViewModel
 * @Description MediaViewModel get media data
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
        MediaStore.Images.Media.DATE_TAKEN
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
        getMediaAsync {
            val medias = queryMedia()
            _lvMediaData.postValue(medias)
            val mediasFilter = filterDamage(medias)
            if(mediasFilter.size != medias.size) {
                _lvMediaData.postValue(mediasFilter)
            }
            val mediaDirList = getMediaDir(mediasFilter)
            _lvMediaDirData.postValue(mediaDirList)
            registerContentObserver()
        }
    }




    @WorkerThread
    suspend fun queryMedia(uri: Uri = MediaStore.Files.getContentUri("external")): MutableList<MediaItem> {
        var mediaItemList = mutableListOf<MediaItem>()
        withContext(Dispatchers.IO) {
            val sortOrder = mediaProjection[mediaProjection.size - 1] + " DESC"
            val cursor = getApplication<Application>().contentResolver.query(
                uri,
                mediaProjection,
                SelectedMediaManager.selection,
                SelectedMediaManager.selectArgs,
                sortOrder
            )
            while (cursor!!.moveToNext()) {
                //查询数据
                val mediaId: Long =
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
                val mediaTime: Long =
                    cursor.getLong(cursor.getColumnIndexOrThrow(mediaProjection[9]))
                val uri = ContentUris.withAppendedId(uri, mediaId)
                val mediaItem = MediaItem()
                mediaItem.id = mediaId
                mediaItem.pathUri = uri
                mediaItem.bucketId = bucketId
                mediaItem.bucketName = bucketName
                mediaItem.originalPath = imagePath
                mediaItem.width = imageWidth
                mediaItem.height = imageHeight
                mediaItem.size = imageSize
                mediaItem.mineType = mediaMineType
                mediaItem.duration = mediaDuration
                mediaItem.dataToken = mediaTime
                EasyLog.d(TAG,"mediaMineType = $mediaMineType")
                if(mediaItem.isVideo()){
                    if(  mediaDuration > VanGoghConst.VIDEO_MAX_DURATION || mediaDuration === 0L)
                    continue
                }
                mediaItemList.add(mediaItem)
            }
        }
        return mediaItemList
    }


    /**
     *  filterDamage some image  size > 0 but Damage
     */
    @WorkerThread
    private suspend fun filterDamage(mediaList: MutableList<MediaItem>): List<MediaItem> {

        var mediaListNotDamage :List<MediaItem>  = mutableListOf()
        withContext(Dispatchers.IO){
             mediaListNotDamage = mediaList.filterNot {
                it.isImage()  && !ImageUtils.isImage(it.originalPath) //&& it.width === 0
            }
        }
        return mediaListNotDamage
    }
    /**
     *  MediaFolder
     */
    @WorkerThread
    private suspend fun getMediaDir(mediaList: List<MediaItem>): MutableList<MediaDir> {
        var mediaDirList = mutableListOf<MediaDir>()
        val videoDir = MediaDir()
        withContext(Dispatchers.IO) {
            mediaList.forEach {
                val mediaDir = MediaDir()
                mediaDir.id = it.id
                mediaDir.bucketId = it.bucketId
                mediaDir.name = it.bucketName
                mediaDir.dateAdded = it.dataToken
                if (!mediaDirList.contains(mediaDir)) {
                    mediaDir.medias.add(it)
                    mediaDir.setCoverPath(it.pathUri)
                    mediaDirList.add(mediaDir)
                } else {
                    mediaDirList[mediaDirList.indexOf(mediaDir)]
                        .medias.add(it)
                }

                if (it.isVideo()) {
                    videoDir.id = it.id
                    videoDir.bucketId = it.bucketId
                    videoDir.medias.add(it)
                    videoDir.setCoverPath(it.pathUri)
                    videoDir.name =
                        getApplication<Application>().getString(R.string.media_all_video)
                }
            }
            if (!videoDir.isEmpty() && VanGoghConst.MEDIA_TYPE !== VanGoghConst.MediaType.MediaOnlyVideo) {
                mediaDirList.add(0, videoDir)
            }
            if (mediaList.isNotEmpty()) {
                val mediaDir = MediaDir()
                val mediaItem = mediaList.first()
                mediaDir.id = mediaItem.id
                mediaDir.bucketId = mediaItem.bucketId
                mediaDir.name = getApplication<Application>().getString(R.string.media_title_str)
                when (VanGoghConst.MEDIA_TYPE) {
                    VanGoghConst.MediaType.MediaAll -> mediaDir.name =
                        getApplication<Application>().getString(R.string.media_title_str)
                    VanGoghConst.MediaType.MediaOnlyImage -> mediaDir.name =
                        getApplication<Application>().getString(R.string.image_title_str)
                    VanGoghConst.MediaType.MediaOnlyGif -> mediaDir.name =
                        getApplication<Application>().getString(R.string.gif_title_str)
                    VanGoghConst.MediaType.MediaOnlyVideo -> mediaDir.name =
                        getApplication<Application>().getString(R.string.video_title_str)
                }
                mediaDir.dateAdded = mediaItem.dataToken
                mediaDir.setCoverPath(mediaItem.pathUri)
                mediaDir.medias.addAll(mediaList)
                mediaDirList.add(0, mediaDir)
            }
        }
        return mediaDirList
    }

    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

}