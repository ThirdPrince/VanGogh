package com.vangogh.media.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.media.vangogh.R
import com.vangogh.media.itf.OnItemClickListener
import com.vangogh.media.models.MediaItem


/**
 * @ClassName GalleryActivity
 * @Description media preview
 * @Author dhl
 * @Date 2020/1/7 9:36
 * @Version 1.0
 */
class MediaPreviewAdapter(private val activity: Activity, var items: List<MediaItem>) :RecyclerView.Adapter<MediaPreviewAdapter.MediaViewHolder>() {



    companion object{
        const val TAG = "MediaPreviewAdapter"
    }
    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

    var onItemClickListener:OnItemClickListener ?=null




    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view){

       // var rootView = view.findViewById<FrameLayout>(R.id.root)
        var photoView: PhotoView = view.findViewById(R.id.photo_view)

        /*override fun onClick(v: View?) {
            onItemClickListener!!.onItemClick(v,adapterPosition)

        }*/


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaPreviewAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.item_preview_pages, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaPreviewAdapter.MediaViewHolder, position: Int) {

        val mediaItem = items[position]
        //Log.e(TAG,"mediaItem = ${mediaItem.toString()}")
        Glide.with(activity).asBitmap().load(mediaItem.path).transition(withCrossFade()).into(holder.photoView)
    }

}