package com.vangogh.media.viewholder

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.media.vangogh.R
import com.vangogh.media.config.VanGogh.selectMediaList
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.view.AnimateCheckBox
import com.vangogh.media.view.CheckView

/**
 * @ClassName CameraViewHolder
 * @Description for camera
 * @Author dhl
 * @Date 2021/2/22 10:25
 * @Version 1.0
 */
open class CameraViewHolder(
    var activity: Activity,
    val view: View,
    var isAvatar: Boolean,
    var onMediaItemClickListener: OnMediaItemClickListener,
    var onItemCheckListener: OnItemCheckListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {


    }

    override fun onClick(v: View?) {
        when (v) {

        }

    }

}