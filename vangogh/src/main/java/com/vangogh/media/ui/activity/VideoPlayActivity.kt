package com.vangogh.media.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.github.chrisbanes.photoview.PhotoView
import com.media.vangogh.R
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.vangogh.media.models.MediaItem

/**
 * @ClassName VideoPlayActivity
 * @Description videoPlay
 * @Author dhl
 * @Date 2021/2/3 10:25
 * @Version 1.0
 */
class VideoPlayActivity : AppCompatActivity() {

    companion object {
        fun actionStart(activity: FragmentActivity, mediaItem: MediaItem) {
            var intent = Intent(activity, VideoPlayActivity::class.java).apply {
                putExtra(MEDIA_ARG, mediaItem)
            }
            activity.startActivity(intent)
        }

        const val MEDIA_ARG = "media"
        private const val UPDATE_UI = 1024
    }


    lateinit var mediaItem: MediaItem


    private val videoPlayer: StandardGSYVideoPlayer by lazy {
        findViewById(R.id.video_player)
    }

    var orientationUtils: OrientationUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_video)
        getData()
        playVideo()
    }


    private fun getData() {
        mediaItem = intent.getParcelableExtra(MEDIA_ARG)!!
    }

    private fun playVideo() {

        val source1 = mediaItem.originalPath
        val photoView = PhotoView(this)
        Glide.with(this).asBitmap().load(mediaItem.originalPath).transition(
            BitmapTransitionOptions.withCrossFade()
        ).into(photoView)
        videoPlayer.thumbImageView = photoView
        videoPlayer.setUp(source1, true, "")
        //设置返回键
        videoPlayer.backButton.visibility = View.VISIBLE
        //设置旋转
        orientationUtils = OrientationUtils(this, videoPlayer)
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)
        //设置返回按键功能
        videoPlayer.backButton.setOnClickListener { onBackPressed() }
        ///不需要屏幕旋转
        videoPlayer.isNeedOrientationUtils = false
        videoPlayer.startPlayLogic()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        if (orientationUtils != null) orientationUtils!!.releaseListener()
    }

    override fun onBackPressed() {
///       不需要回归竖屏
//        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            videoPlayer.getFullscreenButton().performClick();
//            return;
//        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null)
        super.onBackPressed()
    }


}