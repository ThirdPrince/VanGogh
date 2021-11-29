package com.vangogh.media.itf

import android.view.View
import com.vangogh.media.models.MediaItem

/**
 * @ClassName OnSingleMediaResult
 * @Description callback for Media
 * @Author dhl
 * @Date 2021/11/29 10:37
 * @Version 1.0
 */
interface OnSingleMediaResult {
    fun onResult(mediaItem:MediaItem)
}