package com.vangogh.media.adapter

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.github.chrisbanes.photoview.PhotoView
import com.media.vangogh.R
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.MediaGalleryActivity
import java.io.File


/**
 * @ClassName GalleryActivity
 * @Description media preview
 * @Author dhl
 * @Date 2020/1/7 9:36
 * @Version 1.0
 */
class MediaPreviewAdapter(private val activity: AppCompatActivity, var items: List<MediaItem>) :
    RecyclerView.Adapter<MediaPreviewAdapter.MediaViewHolder>() {


    companion object {
        const val TAG = "MediaPreviewAdapter"
    }

    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(activity) }

    var onMediaItemClickListener: OnMediaItemClickListener? = null


    inner class MediaViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var photoView: PhotoView = PhotoView(activity)
            //view.findViewById(R.id.photo_view)
        var rootGroup:FrameLayout = view.findViewById(R.id.root)
        var ivPlay: ImageView = view.findViewById(R.id.iv_play)

        init {
            rootGroup.addView(photoView)
           // photoView.layoutParams = ViewGroup.LayoutParams(-1, -1)
            ivPlay.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v?.id === R.id.iv_play) {
                //VideoPlayActivity.actionStart(activity, items[adapterPosition])
                val intent = Intent(Intent.ACTION_VIEW)
                val uri: Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //判断版本是否在7.0以上
                    uri = FileProvider.getUriForFile(
                        activity,
                        activity.packageName + ".vangogh.provider",
                        File(items[adapterPosition].originalPath)
                    )
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } else {
                    uri = Uri.fromFile(File(items[adapterPosition].originalPath))
                }
                // Video files
                intent.setDataAndType(uri, "video/*")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                activity.startActivity(intent)

            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaPreviewAdapter.MediaViewHolder {

        return MediaViewHolder(mInflater.inflate(R.layout.item_preview_pages, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MediaPreviewAdapter.MediaViewHolder, position: Int) {

        val mediaItem = items[position]
        if (mediaItem.isGif()) {
            Glide.with(activity).asGif().load(mediaItem.originalPath).into(holder.photoView)
        } else {
            Glide.with(activity).asBitmap().load(mediaItem.originalPath).transition(withCrossFade())
                .into(holder.photoView)
        }
        if (mediaItem.isVideo()) {
            holder.ivPlay.visibility = View.VISIBLE
        } else {
            holder.ivPlay.visibility = View.GONE
        }

    }

}