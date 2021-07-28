package com.vangogh.media.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import id.zelory.compressor.Compressor
import kotlinx.coroutines.*

/**
 * @ClassName MediaBaseViewModel
 * @Description MediaBaseViewModel
 * @Author dhl
 * @Date 2021/1/28 10:30
 * @Version 1.0
 */
open class MediaBaseViewModel(application: Application) : AndroidViewModel(application) {


    fun getMediaAsync(block: suspend (scope: CoroutineScope) -> Unit): Job {
        return viewModelScope.launch {
            block(this)
        }
    }


}