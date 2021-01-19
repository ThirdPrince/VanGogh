package com.vangogh.media.itf

import android.view.View
import com.vangogh.media.models.MediaItem

/**
 * @ClassName OnMediaResult
 * @Description TODO
 * @Author aa
 * @Date 2021/1/19 10:37
 * @Version 1.0
 */
interface OnMediaResult {
    fun onResult(mediaList:List<MediaItem>)
}