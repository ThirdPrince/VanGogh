package com.vangogh.media.viewmodel

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.models.MediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @ClassName MediaGridItemAdapter
 * @Description  SelectMediaViewModel viewModel
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class SelectMediaViewModel(application: Application) :MediaBaseViewModel(application){

    companion object {
        const val TAG = "SelectMediaViewModel"
    }
    private val _lvMediaData = MutableLiveData<List<MediaItem>>()

    val lvMediaData: LiveData<List<MediaItem>>
        get() = _lvMediaData

    fun selectMedia(mediaList:MutableList<MediaItem>) {
        launchDataLoad {
            val medias = compressImage(mediaList)
            _lvMediaData.postValue(medias)

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