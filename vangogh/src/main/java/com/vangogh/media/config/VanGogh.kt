package com.vangogh.media.config

import android.app.Activity
import android.provider.MediaStore
import androidx.fragment.app.FragmentActivity
import com.vangogh.media.fragment.CameraFragment
import com.vangogh.media.itf.OnAvatarResult
import com.vangogh.media.itf.OnCameraResult
import com.vangogh.media.itf.OnMediaResult
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.SelectMediaActivity
import com.vangogh.media.utils.MediaQueryConditions
import com.vangogh.media.utils.SelectedMediaManager.selectArgs
import com.vangogh.media.utils.SelectedMediaManager.selectMediaList
import com.vangogh.media.utils.SelectedMediaManager.selection

/**
 * @ClassName VanGogh
 * @Description vanGogh builder
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
object VanGogh {

    private const val TAG = "VanGogh"


    private var fragmentActivity: FragmentActivity? = null


    fun setMaxMediaCount(maxCount: Int): VanGogh {
        VanGoghConst.MAX_MEDIA = maxCount
        return this
    }

    fun setRowCount(spanCount: Int): VanGogh {
        VanGoghConst.GRID_SPAN_CONT = spanCount
        return this
    }

    /**
     * max media size
     */
    fun setMediaMaxSize(maxSize: Int): VanGogh {
        VanGoghConst.MEDIA_MAX_SIZE = maxSize
        selectArgs = arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            VanGoghConst.MEDIA_MAX_SIZE.toString()
        )
        return this
    }

    /**
     * maxDuration s
     */
    fun setVideoMaxDuration(maxDuration: Int): VanGogh {
        VanGoghConst.VIDEO_MAX_DURATION = maxDuration * 1000 + 500
        //selectArgs = MEDIA_SELECTION_ARGS
        return this
    }


    /**
     * all media image + gif +video
     */
    fun getMedia(containsGif: Boolean = true): VanGogh {
        if (!containsGif) {
            selection = MediaQueryConditions.MEDIA_SELECTION_NOT_GIF
            selectArgs = MediaQueryConditions.MEDIA_SELECTION_ARGS_NOT_GIF
        } else {
            selection = MediaQueryConditions.MEDIA_SELECTION
            selectArgs = MediaQueryConditions.MEDIA_SELECTION_ARGS
        }
        VanGoghConst.MEDIA_TYPE = VanGoghConst.MediaType.MediaAll
        return this
    }


    /**
     * contains gif without video
     */
    fun onlyImage(containsGif: Boolean = true): VanGogh {

        if (containsGif) {
            selection = MediaQueryConditions.IMAGE_SELECTION
            selectArgs = MediaQueryConditions.IMAGE_SELECTION_ARGS
        } else {
            selection = MediaQueryConditions.IMAGE_SELECTION_NOT_GIF
            selectArgs = MediaQueryConditions.GIF_SELECTION_ARGS
        }
        VanGoghConst.MEDIA_TYPE = VanGoghConst.MediaType.MediaOnlyImage
        return this
    }


    /**
     * just gif
     */
    fun onlyGif(): VanGogh {
        selection = MediaQueryConditions.GIF_SELECTION
        selectArgs = MediaQueryConditions.GIF_SELECTION_ARGS
        VanGoghConst.MEDIA_TYPE = VanGoghConst.MediaType.MediaOnlyGif
        return this
    }

    /**
     * just video
     */
    fun onlyVideo(): VanGogh {
        selection = MediaQueryConditions.VIDEO_SELECTION
        selectArgs = MediaQueryConditions.VIDEO_SELECTION_ARGS
        VanGoghConst.MEDIA_TYPE = VanGoghConst.MediaType.MediaOnlyVideo
        return this
    }

    /**
     * default complete
     */
    fun setMediaTitleSend(): VanGogh {
        VanGoghConst.MEDIA_TITLE = VanGoghConst.MediaTitle.MediaSend
        return this
    }

    fun enableCamera(): VanGogh {
        VanGoghConst.CAMERA_ENABLE = true
        return this
    }

    fun setSelectedMedia(selectedMedia: MutableList<MediaItem>): VanGogh {
        selectMediaList.clear()
        selectMediaList.addAll(selectedMedia)
        return this
    }


    /**
     * startForMedia result
     * onResult mediaList
     */
    fun startForResult(context: FragmentActivity, onMediaResult: OnMediaResult): VanGogh {

        SelectMediaActivity.actionStart(context, onMediaResult, false)
        fragmentActivity = context
        // mOnMediaResult = onMediaResult
        return this
    }


    /**
     * startForAvatar result
     * onResult mediaItem
     */
    fun startForAvatarResult(context: FragmentActivity, onAvatarResult: OnAvatarResult): VanGogh {

        selection = MediaQueryConditions.IMAGE_SELECTION_NOT_GIF
        selectArgs = MediaQueryConditions.GIF_SELECTION_ARGS
        VanGoghConst.MEDIA_TYPE = VanGoghConst.MediaType.MediaOnlyImage
        SelectMediaActivity.actionStart(context, onAvatarResult, true)
        fragmentActivity = context
        selectMediaList = mutableListOf()
        //mOnAvatarResult = onAvatarResult
        return this
    }


    /**
     * startForCameraResult result
     * onResult mediaItem
     */
    fun startForCameraResult(context: FragmentActivity, onCameraResult: OnCameraResult): VanGogh {

        val cameraFragment = CameraFragment()
        cameraFragment.mOnCameraResult = onCameraResult
        context.supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, CameraFragment(), CameraFragment.TAG)
            .commitAllowingStateLoss()
        fragmentActivity = context

        return this
    }

    /**
     * latest image
     */
    //fun startForLatestImage()


}
