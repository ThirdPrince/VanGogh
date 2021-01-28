package com.vangogh.media.viewmodel

import android.app.Application
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.models.MediaItem
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.*
import java.io.File

/**
 * @ClassName MediaGridItemAdapter
 * @Description  SelectMediaViewModel viewModel
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */

private const val  compressCachePath = "vanGoghCache"

class SelectMediaViewModel(application: Application) : MediaBaseViewModel(application) {

    companion object {
        const val TAG = "SelectMediaViewModel"
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


    /**
     * async compress
     */
    fun selectMedia(mediaList: MutableList<MediaItem>) {


        launchDataLoad {
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
            var startTime = System.currentTimeMillis()
            mediaList.forEach { it ->
                actualImage = File(it.path)
                actualImage?.let { imageFile ->
                    if (it.isImage()) {
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
                Log.e(TAG, "deferred result = ${compressedImage!!.absolutePath}")
                mediaList.forEach {
                    if(it.path!!.endsWith(compressedImage!!.name)){
                        it.compressPath = compressedImage!!.absolutePath
                    }
                }

            }
            var endTime = System.currentTimeMillis()
            Log.e(TAG, "compress Time = ${endTime - startTime}")
            _lvMediaData.postValue(mediaList)
            //VanGogh.selectMediaList.clear()
        }

    }

    private fun getCompressImageFile(actualImage: File): File {
        return File("${imageCompressFile?.absolutePath}${File.separator}${actualImage!!.name}")
    }


}