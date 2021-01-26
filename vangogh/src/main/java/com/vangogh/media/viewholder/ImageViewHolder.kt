package com.vangogh.media.viewholder

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.media.vangogh.R
import com.vangogh.media.config.VanGogh.selectMediaList
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.GalleryActivity
import com.vangogh.media.view.AnimateCheckBox

/**
 * @ClassName VideoViewHolder
 * @Description for video
 * @Author dhl
 * @Date 2021/1/26 10:25
 * @Version 1.0
 */
open class ImageViewHolder( var activity: Activity,view: View,var onMediaItemClickListener: OnMediaItemClickListener,var onItemCheckListener:OnItemCheckListener): RecyclerView.ViewHolder(view), View.OnClickListener{

    private var requestOptions = RequestOptions.centerCropTransform()
        .placeholder(R.drawable.image_grid_placeholder).error(R.drawable.image_grid_placeholder)

    var squareImageView: ImageView
    var checkBox: AnimateCheckBox


    init {
        view.setOnClickListener(this)
        squareImageView = view.findViewById(R.id.iv_content_image)
        checkBox = view.findViewById(R.id.checkbox) as AnimateCheckBox

    }

    override fun onClick(v: View?) {
        onMediaItemClickListener?.onItemClick(v, adapterPosition)
    }

     open fun bindData(mediaItem: MediaItem){
        Glide.with(activity).asBitmap().load(mediaItem.path).transition(BitmapTransitionOptions.withCrossFade())
            .apply(requestOptions).into(squareImageView)

       checkBox.setOnCheckedChangeListener(null)
       checkBox.isChecked = selectMediaList.contains(mediaItem)
       checkBox.setOnCheckedChangeListener(object :
            AnimateCheckBox.OnCheckedChangeListener {
            override fun onCheckedChanged(checkBox: AnimateCheckBox, isChecked: Boolean) {
                onItemCheckListener.onItemCheckClick(checkBox, adapterPosition,isChecked)
            }

        })

    }
}