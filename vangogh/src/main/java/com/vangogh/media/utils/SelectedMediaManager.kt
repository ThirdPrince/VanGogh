package com.vangogh.media.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.MediaTimeUtils.getCameraTime
import java.io.File
import java.io.IOException


/**
 * @ClassName CameraManager
 * @Description dhl
 * @Author dhl
 * @Date 2021/2/23 9:42
 * @Version 1.0
 */
object SelectedMediaManager  {





     var selectMediaList = mutableListOf<MediaItem>()

    /**
     * that selectMedia UI
     */

     var selectMediaActivity = mutableListOf<Activity>()


    /**
     * default select conditions
     */

     var selection = MediaQueryConditions.MEDIA_SELECTION

     var selectArgs = MediaQueryConditions.MEDIA_SELECTION_ARGS













}