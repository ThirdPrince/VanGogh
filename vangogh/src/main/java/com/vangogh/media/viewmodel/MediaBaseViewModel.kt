package com.vangogh.media.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

open class MediaBaseViewModel(application: Application):AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        t.printStackTrace()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob + exceptionHandler)

    private val _lvError = MutableLiveData<Exception>()

    open val lvError: LiveData<Exception>
        get() = _lvError

    fun launchDataLoad(block: suspend (scope: CoroutineScope) -> Unit): Job {
        return viewModelScope.launch {
            try {
                block(this)
            } catch (error: Exception) {
                handleException(error)
            } finally {
            }
        }
    }

    private fun handleException(error: Exception) {
        error.printStackTrace()
        if (error !is CancellationException) {
            _lvError.value = error
        }
    }

    public override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}