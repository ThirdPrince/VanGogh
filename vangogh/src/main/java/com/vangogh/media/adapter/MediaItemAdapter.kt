package com.vangogh.media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.request.transition.DrawableCrossFadeTransition
import com.media.vangogh.R
import com.vangogh.media.models.MediaItem
import com.vangogh.media.view.SquareImageView


/**
 * @author dhl
 * Meidia Grid preview
 *
 */
class MediaItemAdapter(private val fragment: Fragment,var items: List<MediaItem>) :RecyclerView.Adapter<MediaItemAdapter.MediaViewHolder> (){


    private val mInflater: LayoutInflater = LayoutInflater.from(fragment.context)


    private  var requestOptions = RequestOptions.centerCropTransform()
            .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)



    inner class MediaViewHolder(view: View):RecyclerView.ViewHolder(view){

        var squareImageView :ImageView
        init {
            squareImageView = view.findViewById(R.id.iv_content_image) as ImageView
        }


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaItemAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.item_content_image_view, parent, false))
    }

    override fun getItemCount(): Int {
       return items.size
    }

    override fun onBindViewHolder(holder: MediaItemAdapter.MediaViewHolder, position: Int) {

        val mediaItem = items[position]
        Glide.with(fragment).asBitmap().load(mediaItem.path).transition(withCrossFade()).apply(requestOptions).into(holder.squareImageView)
    }
}