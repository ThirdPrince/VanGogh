package com.vangogh.media.viewholder

import android.app.Activity
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.view.AnimateCheckBox

/**
 * @ClassName VideoViewHolder
 * @Description for video
 * @Author dhl
 * @Date 2021/1/26 10:25
 * @Version 1.0
 */
class VideoTypeViewHolder( activity: Activity, view: View, isAvatar:Boolean, onMediaItemClickListener: OnMediaItemClickListener,  onItemCheckListener: OnItemCheckListener): ImageViewHolder(  activity,view, isAvatar,onMediaItemClickListener, onItemCheckListener){

    var durationTv: TextView = view.findViewById(R.id.durationTv) as TextView

    override fun bindData(mediaItem: MediaItem) {
        super.bindData(mediaItem)
        durationTv.text = DateUtils.formatElapsedTime((mediaItem.duration / 1000))
    }



}

