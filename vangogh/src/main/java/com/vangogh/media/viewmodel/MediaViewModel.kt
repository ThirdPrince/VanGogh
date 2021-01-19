package com.vangogh.media.viewmodel

import android.app.Application
import android.content.ContentUris
import android.database.ContentObserver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.extend.registerObserver
import com.vangogh.media.models.MediaDirectory
import com.vangogh.media.models.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author dhl
 * query Media Model
 */
class MediaViewModel(application: Application) :MediaBaseViewModel(application){

    companion object {
        const val TAG = "MediaViewModel"
    }
    private val _lvMediaData = MutableLiveData<List<MediaItem>>()
    val lvMediaData: LiveData<List<MediaItem>>
        get() = _lvMediaData



    private val _lvDataChanged = MutableLiveData<Boolean>()
    val lvDataChanged: LiveData<Boolean>
        get() = _lvDataChanged

    private var contentObserver: ContentObserver? = null


    /**
     * 至少需要高宽，时间
     */
    private val mediaProjection = arrayOf( //查询图片需要的数据列
        BaseColumns._ID,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,  //图片的显示名称  aaa.jpg
        //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
        MediaStore.Images.Media.SIZE,  //图片的大小，long型  132492
        MediaStore.Images.Media.WIDTH,  //图片的宽度，int型  1920
        MediaStore.Images.Media.HEIGHT,  //图片的高度，int型  1080
        MediaStore.Images.Media.MIME_TYPE,  //图片的类型     image/jpeg
        MediaStore.Images.Media.DATE_TAKEN //图片被添加的时间，long型  1450518608

    )

    private fun registerContentObserver(){
        if (contentObserver == null) {
            contentObserver = getApplication<Application>().contentResolver.registerObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ) {
                _lvDataChanged.value = true
            }
        }
    }

    fun getMedia(bucketId: String? = null, mediaType: Int = VanGoghConst.MEDIA_TYPE_IMAGE) {
        launchDataLoad {
            val medias = queryImages(bucketId,mediaType)
            _lvMediaData.postValue(medias)
            registerContentObserver()
        }
    }



    @WorkerThread
    suspend fun queryImages(bucketId: String?, mediaType: Int): MutableList<MediaItem> {
        var data = mutableListOf<MediaItem>()
        withContext(Dispatchers.IO) {
            val uri = MediaStore.Files.getContentUri("external")
            val sortOrder = MediaStore.Images.Media._ID + " DESC"

            var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

            if (mediaType == VanGoghConst.MEDIA_TYPE_VIDEO) {
                selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            }

           /* if (!PickerManager.isShowGif) {
                selection += " AND " + MediaStore.Images.Media.MIME_TYPE + "!='" + MimeTypeMap.getSingleton().getMimeTypeFromExtension("gif") + "'"
            }*/

            if (bucketId != null)
                selection += " AND " + MediaStore.Images.Media.BUCKET_ID + "='" + bucketId + "'"


            val cursor = getApplication<Application>().contentResolver.query(uri, mediaProjection, selection, null, sortOrder)

            while (cursor!!.moveToNext()) {
                //查询数据
                val imageId: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(mediaProjection[0]))
                val imagePath: String =
                    cursor.getString(cursor.getColumnIndexOrThrow(mediaProjection[1]))
                var bucketName:String =
                   cursor.getString(cursor.getColumnIndexOrThrow(mediaProjection[2]))
                val imageSize: Long =
                    cursor.getLong(cursor.getColumnIndexOrThrow(mediaProjection[3]))
                val imageWidth: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(mediaProjection[4]))
                val imageHeight: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(mediaProjection[5]))
                val imageMimeType: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(mediaProjection[6]))
                val mediaItem = MediaItem()
                mediaItem.path = imagePath
                mediaItem.width = imageWidth
                mediaItem.height = imageHeight
                mediaItem.size = imageSize
               // Log.e(TAG,"path = $imagePath:::imageSize = $imageSize:::width = $imageWidth:: height = $imageHeight")
                if(imageSize == 0L && imageWidth == 0 && imageHeight == 0)
                    continue
                data.add(mediaItem)
            }
        }
        return data
    }

    @WorkerThread
    private fun getPhotoDirectories(fileType: Int, data: Cursor): MutableList<MediaDirectory> {
        var directories = mutableListOf<MediaDirectory>()
        while (data.moveToNext()) {

            val imageId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID))
            val bucketId = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID))
            val name = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
            val fileName = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
            val mediaType = data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
            val mediaPath = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_TAKEN))

            var contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageId
            )
            if (fileType == VanGoghConst.MEDIA_TYPE_VIDEO) {
                contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )
            }

        }

        return directories
    }

    override fun onCleared() {
        contentObserver?.let {
            getApplication<Application>().contentResolver.unregisterContentObserver(it)
        }
    }

}