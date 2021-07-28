package com.vangogh.media.viewmodel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.core.log.EasyLog
import com.vangogh.media.cache.LRUImageCache
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst.MAX_IMG_CACHE_SIZE
import com.vangogh.media.models.MediaItem
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.util.LinkedHashMap

/**
 * @ClassName MediaGridItemAdapter
 * @Description  CompressMediaViewModel viewModel just compress image
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */

private const val  compressCachePath = "vanGoghCache"

private const val compressCacheByImage = "imageCache"


class CompressMediaViewModel(application: Application) : MediaBaseViewModel(application) {

    companion object {
        const val TAG = "CompressMediaViewModel"
    }

    private val _lvMediaData = MutableLiveData<List<MediaItem>>()

    val lvMediaData: LiveData<List<MediaItem>>
        get() = _lvMediaData

    /**
     *  it to be compress
     */
    private var actualImage: File? = null

    /**
     * compress image destination
     */
    private var compressedImage: File? = null

    /**
     * compressImage root
     */
    private  var imageCompressFile: File?= null

    /**
     * async compressImage
     */
    var deferredList = mutableListOf<Deferred<File>>()




    init {
        imageCompressFile =
            getApplication<Application>().getExternalFilesDir(compressCachePath)!!
    }

    var lruImageCache = LRUImageCache<String,Long>(MAX_IMG_CACHE_SIZE)

    var sp: SharedPreferences =
        getApplication<Application>().getSharedPreferences("SaveMap", Context.MODE_PRIVATE)
    var editor: SharedPreferences.Editor = sp.edit()



    /**
     * async compress
     */
    fun selectMedia(mediaList: MutableList<MediaItem>) {

        getMediaAsync {
            var startTime = System.currentTimeMillis()
            mediaList.forEach {
                actualImage = File(it.path)
                actualImage?.let { imageFile ->
                    val file = getCompressImageFile(actualImage!!)
                    if (!file.exists()) {

                        compressedImage =
                            Compressor.compress(getApplication<Application>(), imageFile) {
                                default()
                                val file = getCompressImageFile(actualImage!!)
                                destination(file)

                            }
                        it.compressPath = compressedImage!!.absolutePath
                    } else {
                        it.compressPath = file.absolutePath
                    }

                }
            }
            var endTime = System.currentTimeMillis()
            Log.e(TAG, "compress Time = ${endTime - startTime}")
            _lvMediaData.postValue(mediaList)

        }
    }

    /**
     * async compress
     * only compress image (exclude gif video)
     */
    fun compressImage(mediaList: MutableList<MediaItem>) {
        viewModelScope.launch {
            mediaList.forEach { it ->
                actualImage = File(it.path)
                actualImage?.let { imageFile ->
                    if (it.isCompress()) {
                        val file = getCompressImageFile(imageFile)
                        if (file.exists()) {
                            it.compressPath = file.absolutePath
                        } else {
                            val compressedImage = async {
                                Compressor.compress(getApplication<Application>(), imageFile) {
                                    default()
                                    val file = getCompressImageFile(imageFile!!)
                                    destination(file)
                                }
                            }
                            deferredList.add(compressedImage)
                        }
                    }else{
                        it.compressPath = it.path
                    }
                }

            }

            deferredList.forEachIndexed { index, deferred ->
                compressedImage = deferred.await()
                mediaList.forEach {
                    if(it.path!!.endsWith(compressedImage!!.name)){
                        it.compressPath = compressedImage!!.absolutePath
                    }

                }

            }
            EasyLog.e(TAG,"files = ${imageCompressFile?.listFiles()?.size!!}")
            if(imageCompressFile?.listFiles()?.size!! > MAX_IMG_CACHE_SIZE) {
                lruImageCache = getMap(compressCacheByImage)!!
                mediaList.forEach {
                    lruImageCache[it.compressPath!!] = File(it.compressPath).length()
                }
                saveMap(compressCacheByImage,lruImageCache)
            }
            EasyLog.e(TAG,"files = ${imageCompressFile?.listFiles()?.size!!}")
            EasyLog.e(TAG,lruImageCache.size.toString())
            _lvMediaData.postValue(mediaList)
        }

    }

    private fun getCompressImageFile(actualImage: File): File {
        return File("${imageCompressFile?.absolutePath}${File.separator}${actualImage!!.name}")
    }

   private suspend fun saveMap(key: String, datas: LRUImageCache<String, Long>) = withContext(Dispatchers.IO){
        val mJsonArray = JSONArray()
        val iterator: Iterator<Map.Entry<String, Long>> = datas.entries.iterator()
        val jsonObject = JSONObject()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            try {
                jsonObject.put(entry.key, entry.value)
            } catch (e: JSONException) {
            }
        }
        mJsonArray.put(jsonObject)
        editor.putString(key, mJsonArray.toString())
        editor.commit()
    }
    private suspend fun getMap(key: String): LRUImageCache<String, Long>?= withContext(Dispatchers.IO) {
        val datas: LRUImageCache<String, Long> = LRUImageCache(MAX_IMG_CACHE_SIZE)
        val result: String? = sp.getString(key, "")
        if(TextUtils.isEmpty(result)){
            return@withContext datas
        }
        try {
            val array = JSONArray(result)
            for (i in 0 until array.length()) {
                val itemObject = array.getJSONObject(i)
                val names = itemObject.names()
                if (names != null) {
                    for (j in 0 until names.length()) {
                        val name = names.getString(j)
                        val value = itemObject.getLong(name)
                        datas[name] = value
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return@withContext datas
    }

}