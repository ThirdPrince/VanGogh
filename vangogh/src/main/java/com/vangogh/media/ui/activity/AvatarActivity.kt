package com.vangogh.media.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.extend.toast
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.MediaPreviewUtil


/**
 * @ClassName AvatarActivity
 * @Description image for avatar
 * @Author dhl
 * @Date 2021/2/18 9:36
 * @Version 1.0
 */
class AvatarActivity : BaseSelectActivity() {

    companion object {

        /**
         * media index
         */
        const val MEDIA_POS = "mediaPos"


        const val REQUEST_CODE = 1024

        fun actionStart(
            activity: AppCompatActivity,
            mediaPos: Int,
            isAvatar: Boolean
        ) {
            var intent = Intent(activity, AvatarActivity::class.java).apply {
                putExtra(MEDIA_POS, mediaPos)
                putExtra(SelectMediaActivity.IS_AVATAR, isAvatar)
            }
            activity.startActivityForResult(intent, REQUEST_CODE)

        }


    }


    private val topBarRoot by lazy { findViewById<RelativeLayout>(R.id.top_bar_root) }


    private val photoView by lazy {
        findViewById<PhotoView>(R.id.photo_view)
    }

    private var currentMedia: MediaItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMediaIndex()
        initListener()
    }

    override fun contentLayout(): Int {
        return R.layout.activity_avatar
    }

    override fun backPress() {
      finish()
    }


    private fun initListener() {
        topBarRoot.setOnClickListener {
            finish()
        }
    }

    private fun setMediaIndex() {
        currentMedia = MediaPreviewUtil.currentMediaList!![mediaPos]
        mediaSend?.isEnabled = true
        mediaSend?.visibility = View.VISIBLE
        mediaSend?.text = getString(R.string.media_done)
        Glide.with(this).load(currentMedia?.pathUri).into(photoView)
    }


}