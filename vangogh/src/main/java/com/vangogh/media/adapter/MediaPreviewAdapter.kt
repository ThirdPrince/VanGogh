package com.vangogh.media.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.media.vangogh.R
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.VideoPlayActivity


/**
 * @ClassName GalleryActivity
 * @Description media preview
 * @Author dhl
 * @Date 2020/1/7 9:36
 * @Version 1.0
 */
class MediaPreviewAdapter(private val activity: FragmentActivity, var items: List<MediaItem>) :RecyclerView.Adapter<MediaPreviewAdapter.MediaViewHolder>() {



    companion object{
        const val TAG = "MediaPreviewAdapter"
    }
    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

    var onMediaItemClickListener:OnMediaItemClickListener ?=null




    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnClickListener{

        var photoView: PhotoView = view.findViewById(R.id.photo_view)
        var ivPlay: ImageView = view.findViewById(R.id.iv_play)
        init {
            ivPlay.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
           if(v?.id === R.id.iv_play ){
               VideoPlayActivity.actionStart(activity,items[adapterPosition])
           }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPreviewAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.item_preview_pages, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaPreviewAdapter.MediaViewHolder, position: Int) {

        val mediaItem = items[position]
        if(mediaItem.isGif()){
            Glide.with(activity).asGif().load(mediaItem.path).into(holder.photoView)
        }else {
            Glide.with(activity).asBitmap().load(mediaItem.path).transition(withCrossFade())
                .into(holder.photoView)
        }
        if(mediaItem.isVideo()){
            holder.ivPlay.visibility = View.VISIBLE
        }else{
            holder.ivPlay.visibility = View.GONE
        }

    }

}