package com.vangogh.media.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.github.chrisbanes.photoview.PhotoView
import com.media.vangogh.R
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaDir
import com.vangogh.media.models.MediaItem


/**
 * @ClassName MediaDirAdapter
 * @Description media mediaDir adapter
 * @Author dhl
 * @Date 2020/1/7 9:36
 * @Version 1.0
 */
class MediaDirAdapter(private val context: Context, var items: List<MediaDir>) :RecyclerView.Adapter<MediaDirAdapter.MediaViewHolder>() {



    companion object{
        const val TAG = "MediaPreviewAdapter"
    }
    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }

    var onMediaItemClickListener:OnMediaItemClickListener ?=null




    inner class MediaViewHolder(val view: View) : RecyclerView.ViewHolder(view),View.OnClickListener{

        var coverImage: ImageView = view.findViewById(R.id.iv_cover)
        var dirName :TextView = view.findViewById(R.id.tv_folder_name)
        init {
            view.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            onMediaItemClickListener?.onItemClick(view,adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaDirAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.media_dir_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaDirAdapter.MediaViewHolder, position: Int) {

        val mediaDir = items[position]
        holder.dirName.text = mediaDir.name + "( ${mediaDir.medias.size} )"
        Glide.with(context).asBitmap().load(mediaDir.getCoverPath()).transition(withCrossFade()).into(holder.coverImage)
    }

}