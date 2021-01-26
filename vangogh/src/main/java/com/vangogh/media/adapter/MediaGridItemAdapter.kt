package com.vangogh.media.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.media.vangogh.R
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
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




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageViewHolder {
        return when (viewType) {

            VIDEO -> VideoTypeViewHolder(activity,
                mInflater.inflate(
                    R.layout.item_content_video_view,
                    parent,
                    false
                ),onMediaItemClickListener!!,onItemCheckListener!!
            )
            GIF -> ImageViewHolder(activity,
                mInflater.inflate(
                    R.layout.item_content_gif_view,
                    parent,
                    false
                ),onMediaItemClickListener!!,onItemCheckListener!!
            )
            else -> ImageViewHolder(activity,
                mInflater.inflate(
                    R.layout.item_content_image_view,
                    parent,
                    false
                ),onMediaItemClickListener!!,onItemCheckListener!!
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
        holder.bindData(mediaItem)
    }



    companion object {
        const val IMAGE = 1
        const val GIF = 2
        const val VIDEO = 3
    }
}