package com.vangogh.media.config

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import androidx.annotation.IntegerRes
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vangogh.media.itf.OnMediaResult
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.SelectMediaActivity
import com.vangogh.media.viewmodel.SelectMediaViewModel
import java.util.ArrayList

/**
 * @ClassName MediaGridItemAdapter
 * @Description vanGogh builder
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
object VanGogh {

    const val TAG = "VanGogh"

    var onMediaResult: OnMediaResult? = null


    val _lvMediaData = MutableLiveData<List<MediaItem>>()

    val lvMediaData: LiveData<List<MediaItem>>
        get() = _lvMediaData

    private var fragmentActivity:FragmentActivity?= null




    fun setMaxCount(maxCount: Int): VanGogh {
        //  PickerManager.setMaxCount(maxCount)
        return this
    }

    fun setActivityTheme(theme: Int): VanGogh {
        // PickerManager.theme = theme
        return this
    }

    fun setActivityTitle(title: String): VanGogh {
        //PickerManager.title = title
        return this
    }

    fun setSelectedFiles(selectedPhotos: ArrayList<Uri>): VanGogh {
        //  mPickerOptionsBundle.putParcelableArrayList(FilePickerConst.KEY_SELECTED_MEDIA, selectedPhotos)
        return this
    }

    fun enableVideoPicker(status: Boolean): VanGogh {
        // PickerManager.setShowVideos(status)
        return this
    }

    fun enableImagePicker(status: Boolean): VanGogh {
        //   PickerManager.setShowImages(status)
        return this
    }

    fun enableSelectAll(status: Boolean): VanGogh {
        // PickerManager.enableSelectAll(status)
        return this
    }

    fun setCameraPlaceholder(@DrawableRes drawable: Int): VanGogh {
        // PickerManager.cameraDrawable = drawable
        return this
    }

    fun showGifs(status: Boolean): VanGogh {
        //PickerManager.isShowGif = status
        return this
    }

    fun showFolderView(status: Boolean): VanGogh {
        // PickerManager.isShowFolderView = status
        return this
    }

    fun enableDocSupport(status: Boolean): VanGogh {
        //PickerManager.isDocSupport = status
        return this
    }

    fun enableCameraSupport(status: Boolean): VanGogh {
        //  PickerManager.isEnableCamera = status
        return this
    }


    fun withOrientation(@IntegerRes orientation: Int): VanGogh {
        //PickerManager.orientation = orientation
        return this
    }


    /* fun pickMedia(context: Fragment) {
        // mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
         start(context, 0)
     }*/


    fun pickMedia(context: FragmentActivity, selectMediaViewModel: SelectMediaViewModel) {
        // mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(context)
    }

    fun pickMedia(context: Fragment) {
        //  mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(context.requireActivity())
    }

    fun pickMedia(activity: FragmentActivity): VanGogh {
        //  mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER)
        start(activity)
        return this
    }



    private fun start(context: FragmentActivity) {

        SelectMediaActivity.actionStart(context)
        fragmentActivity = context
        lvMediaData.observe(fragmentActivity!!, Observer {
            onMediaResult?.onResult(it)
        })
    }

    /**
     * startForMedia result
     * onResult mediaList
     */
     fun startForResult(context: FragmentActivity):VanGogh {

        SelectMediaActivity.actionStart(context)
        fragmentActivity = context
        lvMediaData.observe(fragmentActivity!!, Observer {
            onMediaResult?.onResult(it)
        })
         return this
    }

}
