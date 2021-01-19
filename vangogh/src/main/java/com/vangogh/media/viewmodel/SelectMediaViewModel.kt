package com.vangogh.media.viewmodel

import android.app.Application
import android.os.Environment
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.models.MediaItem
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @ClassName MediaGridItemAdapter
 * @Description  SelectMediaViewModel viewModel
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class SelectMediaViewModel( application: Application) :MediaBaseViewModel(application){

    companion object {
        const val TAG = "SelectMediaViewModel"
    }
    private val _lvMediaData = MutableLiveData<List<MediaItem>>()

    val lvMediaData: LiveData<List<MediaItem>>
        get() = _lvMediaData

    private var actualImage: File? = null
    private var compressedImage: File? = null

    fun selectMedia(mediaList:MutableList<MediaItem>) {
        launchDataLoad {
            mediaList.forEach {
                actualImage = File(it.path)
                actualImage?.let {imageFile->
                    compressedImage = Compressor.compress(getApplication<Application>(), imageFile){
                        default()
                        getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.also {
                            val file = File("${it.absolutePath}${File.separator}${actualImage!!.name}.${imageFile.extension}")
                            destination(file)
                        }
                    }
                    it.compressPath = compressedImage!!.absolutePath
                }
            }
            _lvMediaData.postValue(mediaList)

        }
    }

    @WorkerThread
    suspend fun compressImage( mediaList:MutableList<MediaItem>): MutableList<MediaItem> {
        var data = mutableListOf<MediaItem>()
        withContext(Dispatchers.IO) {

            }
        
        return mediaList
    }


}