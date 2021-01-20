package com.vangogh.media.viewmodel

import android.app.Application
import android.os.Environment
import android.view.View
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.models.MediaItem
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.android.synthetic.main.activity_select_media.*
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
    private lateinit var imageCompressFile: File


    fun selectMedia(mediaList: MutableList<MediaItem>) {
        imageCompressFile =
            getApplication<Application>().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        launchDataLoad {
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
            _lvMediaData.postValue(mediaList)

        }
    }

    private fun getCompressImageFile(actualImage:File):File{
        return   File("${imageCompressFile.absolutePath}${File.separator}${actualImage!!.name}.${actualImage.extension}")
    }


}