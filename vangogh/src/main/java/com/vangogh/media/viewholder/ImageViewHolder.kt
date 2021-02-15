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
 * @ClassName VideoViewHolder
 * @Description for video
 * @Author dhl
 * @Date 2021/1/26 10:25
 * @Version 1.0
 */
open class ImageViewHolder(
    var activity: Activity,
     val view: View,
    var onMediaItemClickListener: OnMediaItemClickListener,
    var onItemCheckListener: OnItemCheckListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    private var requestOptions = RequestOptions.centerCropTransform()
        .placeholder(R.drawable.image_grid_placeholder).error(R.drawable.image_grid_placeholder)

    private var squareImageView: ImageView
   // private var checkBox: AnimateCheckBox
    private var numCheckBox: CheckView
    private var gifImage: ImageView? = null


    init {
        view.setOnClickListener(this)
        squareImageView = view.findViewById(R.id.iv_content_image)
        //checkBox = view.findViewById(R.id.checkbox) as AnimateCheckBox
        numCheckBox = view.findViewById(R.id.num_checkbox)
        numCheckBox.setCountable(true)
        numCheckBox.setOnClickListener(this)
        gifImage = view.findViewById(R.id.gif_img)

    }

    override fun onClick(v: View?) {
        when(v){
            numCheckBox ->   onItemCheckListener.onItemCheckClick(v, adapterPosition, true)
            view -> onMediaItemClickListener?.onItemClick(v, adapterPosition)
        }

    }

    open fun bindData(mediaItem: MediaItem) {
        Glide.with(activity).asBitmap().load(mediaItem.pathUri  )
            .transition(BitmapTransitionOptions.withCrossFade())
            .apply(requestOptions).into(squareImageView)

        if (selectMediaList.contains(mediaItem)) {
            numCheckBox.isEnabled = true
            if(selectMediaList.size>0) {
                numCheckBox.setCheckedNum(selectMediaList.indexOf(mediaItem)+1)
            }
            //numCheckBox.setChecked(true)
            setMediaMask(true)
        } else {
            numCheckBox.setCheckedNum(CheckView.UNCHECKED)
            setMediaMask(false)
        }
        setGif(mediaItem)
    }

    /**
     * set gif
     */
    private fun setGif(mediaItem: MediaItem) {
        if (mediaItem.isGif()) {
            gifImage?.visibility = View.VISIBLE
        } else {
            gifImage?.visibility = View.GONE
        }
    }

    private fun setMediaMask(boolean: Boolean) {
        if (boolean) {

                squareImageView.setColorFilter(
                    Color.GRAY,
                    PorterDuff.Mode.MULTIPLY
                )

        } else {
            squareImageView.clearColorFilter()
        }
    }
}