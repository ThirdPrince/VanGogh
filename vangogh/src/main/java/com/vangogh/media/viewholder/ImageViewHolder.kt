package com.vangogh.media.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.view.AnimateCheckBox

/**
 * @ClassName VideoViewHolder
 * @Description for video
 * @Author dhl
 * @Date 2021/1/26 10:25
 * @Version 1.0
 */
open class ImageViewHolder( view: View,var onMediaItemClickListener: OnMediaItemClickListener): RecyclerView.ViewHolder(view), View.OnClickListener{


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
}