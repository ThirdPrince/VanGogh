package com.vangogh.media.adapter

import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.media.vangogh.R
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.view.AnimateCheckBox
import com.vangogh.media.viewholder.GifViewHolder
import com.vangogh.media.viewholder.ImageViewHolder
import com.vangogh.media.viewholder.VideoTypeViewHolder


/**
 * @ClassName MediaGridItemAdapter
 * @Description  media grid show
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class MediaGridItemAdapter(private val activity: FragmentActivity, var items: List<MediaItem>) :
    RecyclerView.Adapter<ImageViewHolder>() {


    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

    var onMediaItemClickListener: OnMediaItemClickListener? = null

    var onItemCheckListener: OnItemCheckListener? = null

    private var requestOptions = RequestOptions.centerCropTransform()
        .placeholder(R.drawable.image_grid_placeholder).error(R.drawable.image_grid_placeholder)


    /**
     * checkedList
     */
    var selectMediaList = mutableListOf<MediaItem>()


    /*inner class ImageViewHolder(var view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var squareImageView: ImageView
        var checkBox: AnimateCheckBox
        var imgGif: ImageView

        init {
            view.setOnClickListener(this)
            squareImageView = view.findViewById(R.id.iv_content_image)
            imgGif = view.findViewById(R.id.img_gif)
            checkBox = view.findViewById(R.id.checkbox) as AnimateCheckBox
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClick(v, adapterPosition)
        }

    }*/


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        return when (viewType) {

            VIDEO -> VideoTypeViewHolder(
                mInflater.inflate(
                    R.layout.item_content_video_view,
                    parent,
                    false
                ),onMediaItemClickListener!!
            )
            GIF -> GifViewHolder(
                mInflater.inflate(
                    R.layout.item_content_gif_view,
                    parent,
                    false
                ),onMediaItemClickListener!!
            )
            else -> ImageViewHolder(
                mInflater.inflate(
                    R.layout.item_content_image_view,
                    parent,
                    false
                ),onMediaItemClickListener!!
            )


        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val mediaItem = items[position]
        return when {
            mediaItem.isVideo() -> {
                VIDEO
            }
            mediaItem.isGif() -> {
                GIF
            }
            else -> {
                IMAGE
            }
        }
    }


    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val mediaItem = items[position]

        Glide.with(activity).asBitmap().load(mediaItem.path).transition(withCrossFade())
            .apply(requestOptions).into(holder.squareImageView)

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selectMediaList.contains(mediaItem)
        holder.checkBox.setOnCheckedChangeListener(object :
            AnimateCheckBox.OnCheckedChangeListener {
            override fun onCheckedChanged(checkBox: AnimateCheckBox, isChecked: Boolean) {
                onItemCheckListener?.onItemCheckClick(checkBox, position, isChecked)
            }

        })
        //setGifTag(holder, mediaItem)
    }

    /* */
    /**
     * gif show or hide
     *//*
    private fun setGifTag(holder: ImageViewHolder, mediaItem: MediaItem) {
        holder.imgGif.visibility = if (mediaItem.isGif()) View.VISIBLE
        else View.GONE
    }*/

    companion object {
        const val IMAGE = 1
        const val GIF = 2
        const val VIDEO = 3
    }
}