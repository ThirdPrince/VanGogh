package com.vangogh.media.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.media.vangogh.R
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.itf.OnCameraClickListener
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.viewholder.CameraViewHolder
import com.vangogh.media.viewholder.ImageViewHolder
import com.vangogh.media.viewholder.VideoTypeViewHolder


/**
 * @ClassName MediaGridItemAdapter
 * @Description  media grid show
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class MediaGridItemAdapter(private val activity: FragmentActivity, var items: List<MediaItem>,var isAvatar :Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

    var onMediaItemClickListener: OnMediaItemClickListener? = null

    var onItemCheckListener: OnItemCheckListener? = null

    var onCameraClickListener: OnCameraClickListener? = null


    /**
     * checkedList
     */
    var selectMediaList = mutableListOf<MediaItem>()



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {

            CAMERA -> CameraViewHolder(activity,
                mInflater.inflate(
                    R.layout.item_content_camera_view,
                    parent,
                    false
                ),onCameraClickListener!!
            )
            VIDEO -> VideoTypeViewHolder(activity,
                mInflater.inflate(
                    R.layout.item_content_video_view,
                    parent,
                    false
                ),false,onMediaItemClickListener!!,onItemCheckListener!!
            )
            else -> ImageViewHolder(activity,
                mInflater.inflate(
                    R.layout.item_content_image_view,
                    parent,
                    false
                ),isAvatar,onMediaItemClickListener!!,onItemCheckListener!!
            )


        }

    }

    override fun getItemCount(): Int {
        return if(VanGoghConst.CAMERA_ENABLE){
            items.size+1
        }else{
            items.size
        }

    }

    override fun getItemViewType(position: Int): Int {
        if(VanGoghConst.CAMERA_ENABLE && position == 0){
            return CAMERA
        }
        val mediaItem = getMedia(position)
        return when {
            mediaItem.isVideo() -> {
                VIDEO
            }
            else -> {
                IMAGE
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ImageViewHolder) {
            val mediaItem = getMedia(position)
            //val mediaItem = items[position]
            holder.bindData(mediaItem)
        }
    }

     fun getMedia(position :Int):MediaItem{
        return items[if (VanGoghConst.CAMERA_ENABLE) position - 1 else position]
    }



    companion object {
        const val IMAGE = 1
        const val VIDEO = 2
        const val CAMERA = 3
    }
}