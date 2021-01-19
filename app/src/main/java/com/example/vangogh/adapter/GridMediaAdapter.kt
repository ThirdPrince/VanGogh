package com.example.vangogh.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.vangogh.R
import com.example.vangogh.itf.OnAddMediaListener
import com.vangogh.media.models.MediaItem

/**
 * @ClassName GridImageAdapter
 * @Description just for get Media test
 * @Author dhl
 * @Date 2021/1/16 10:27
 * @Version 1.0
 */
class GridMediaAdapter(private val activity: Activity, var items: List<MediaItem>) : RecyclerView.Adapter<GridMediaAdapter.GridImageViewHolder>() {


    companion object{
        const val TAG = "GridMediaAdapter"
        const val TYPE_ADD_MEDIA = 1
        const val TYPE_MEDIA = 2
        const val MAX_MEDIA_SIZE = 9
    }
     private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

      var onAddMediaListener:OnAddMediaListener?= null


    private var requestOptions = RequestOptions.centerCropTransform()
        .placeholder(R.drawable.image_grid_placeholder).error(R.drawable.image_grid_placeholder)

    inner class GridImageViewHolder( view: View) : RecyclerView.ViewHolder(view) ,View.OnClickListener{
        var squareImageView: ImageView = view.findViewById(R.id.iv_content_image)
        init {
            squareImageView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if(isAddMedia(adapterPosition)) {
                onAddMediaListener?.onAddMediaClick()
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridImageViewHolder {
        return GridImageViewHolder(mInflater.inflate(R.layout.image_grid_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return if (items.size < MAX_MEDIA_SIZE) {
            items.size + 1
        } else {
            items.size
        }
    }

    override fun onBindViewHolder(holder: GridImageViewHolder, position: Int) {

        if(isAddMedia(position)){
            Glide.with(activity).asBitmap().load(R.drawable.ic_add_image)
                .into(holder.squareImageView)
        }else {
            val mediaItem = items[position]
            Glide.with(activity).asBitmap().load(mediaItem.path)
                .transition(BitmapTransitionOptions.withCrossFade())
                .apply(requestOptions).into(holder.squareImageView)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(isAddMedia(position)){
            TYPE_ADD_MEDIA
        }else{
            TYPE_MEDIA
        }

    }
    private fun isAddMedia(position: Int): Boolean {
        val size = if (items.isEmpty()) 0 else items.size
        return position == size
    }
}


