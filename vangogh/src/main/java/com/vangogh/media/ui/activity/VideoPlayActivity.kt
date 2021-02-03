package com.vangogh.media.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.media.vangogh.R
import com.vangogh.media.models.MediaItem

/**
 * @ClassName VideoPlayActivity
 * @Description videoPlay
 * @Author dhl
 * @Date 2021/2/3 10:25
 * @Version 1.0
 */
class VideoPlayActivity : AppCompatActivity() {

    companion object{
        fun actionStart(activity: FragmentActivity, mediaItem: MediaItem){
             var intent = Intent(activity,VideoPlayActivity::class.java).apply {
                 putExtra(MEDIA_ARG,mediaItem)
             }
            activity.startActivity(intent)
        }

        const val MEDIA_ARG = "media"
        private const val UPDATE_UI = 1024
    }

    lateinit var mediaItem: MediaItem
    lateinit var videoView :VideoView
    lateinit var time_current_tv :TextView
    lateinit var time_total_tv :TextView

    private val videoHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what === UPDATE_UI) {
                val currentPosition = videoView.currentPosition
                val totalDuration = videoView.duration
                updateTv(time_current_tv, currentPosition)
                updateTv(time_total_tv, totalDuration)
                sendEmptyMessageDelayed(UPDATE_UI, 500)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        getData()
        initView()

    }
    private fun initView(){
        videoView = findViewById(R.id.video_view)
        time_current_tv = findViewById(R.id.time_current_tv)
        time_total_tv = findViewById(R.id.time_total_tv)
        videoView.setVideoURI(mediaItem.pathUri)
        videoView.start()
        videoHandler.sendEmptyMessageDelayed(UPDATE_UI,500)
    }

    private fun getData(){
        mediaItem = intent.getParcelableExtra(MEDIA_ARG)
    }


    private fun updateTv(tv: TextView, milliSec: Int) {
        val sec = milliSec / 1000
        val hh = sec / 3600
        val mm = sec % 3600 / 60
        val ss = sec % 60
        var str: String? = null
        str = if (hh != 0) {
            String.format("%02d:%02d:%02d", hh, mm, ss)
        } else {
            String.format("%02d:%02d", mm, ss)
        }
        tv.text = str
    }
}