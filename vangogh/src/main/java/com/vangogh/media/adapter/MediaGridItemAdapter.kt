package com.vangogh.media.adapter

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
import com.vangogh.media.models.MediaItem


/**
 * @author dhl
 * MediaItem Grid preview
 *
 */
class MediaGridItemAdapter(private val activity: FragmentActivity, var items: List<MediaItem>) : RecyclerView.Adapter<MediaGridItemAdapter.MediaViewHolder>() {


    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }


    private var requestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)


    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view) {

         val squareImageView: ImageView by lazy { view.findViewById<ImageView>(R.id.iv_content_image) }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaGridItemAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.item_content_image_view, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaGridItemAdapter.MediaViewHolder, position: Int) {

        val mediaItem = items[position]
        Glide.with(activity).asBitmap().load(mediaItem.path).transition(withCrossFade()).apply(requestOptions).into(holder.squareImageView)
    }
}