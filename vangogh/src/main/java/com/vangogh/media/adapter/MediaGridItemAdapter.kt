package com.vangogh.media.adapter

import android.util.Log
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
import com.vangogh.media.adapter.MediaPreviewAdapter.Companion.TAG
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.view.AnimateCheckBox


/**
 * @ClassName MediaGridItemAdapter
 * @Description  media grid show
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class MediaGridItemAdapter(private val activity: FragmentActivity, var items: List<MediaItem>) :
    RecyclerView.Adapter<MediaGridItemAdapter.MediaViewHolder>() {


    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

    var onItemClickListener: OnItemClickListener? = null

    var onItemCheckListener: OnItemCheckListener? = null

    private var requestOptions = RequestOptions.centerCropTransform()
        .placeholder(R.drawable.image_grid_placeholder).error(R.drawable.image_grid_placeholder)


    /**
     * checkedList
     */
    var selectMediaList = mutableListOf<MediaItem>()

    inner class MediaViewHolder(var view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        var squareImageView: ImageView
        var checkBox: AnimateCheckBox

        init {
            view.setOnClickListener(this)
            squareImageView = view.findViewById(R.id.iv_content_image)
            checkBox = view.findViewById(R.id.checkbox) as AnimateCheckBox
        }

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClick(v, adapterPosition)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaGridItemAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.item_content_image_view, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaGridItemAdapter.MediaViewHolder, position: Int) {

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
        // Log.e(TAG, "isChecked = ${selectMediaList.contains(mediaItem)}")
        //

    }
}