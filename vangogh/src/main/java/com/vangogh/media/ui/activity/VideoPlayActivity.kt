package com.vangogh.media.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
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
    lateinit var videoPlay:ImageView
    lateinit var playSeek:AppCompatSeekBar
    lateinit var time_current_tv :TextView
    lateinit var time_total_tv :TextView

    private  var currentPosition  = 0

    private val videoHandler: Handler = object : Handler() {
        @SuppressLint("HandlerLeak")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what === UPDATE_UI) {
                val currentPosition = videoView.currentPosition
                val totalDuration = videoView.duration
                updateTv(time_current_tv, currentPosition)
                updateTv(time_total_tv, totalDuration)
                playSeek.max = totalDuration
                playSeek.progress = currentPosition
                sendEmptyMessageDelayed(UPDATE_UI, 500)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        getData()
        initView()
        initEvent()

    }
    private fun initView(){
        videoPlay = findViewById(R.id.iv_play)
        videoView = findViewById(R.id.video_view)
        playSeek = findViewById(R.id.play_seek)
        time_current_tv = findViewById(R.id.time_current_tv)
        time_total_tv = findViewById(R.id.time_total_tv)
        videoView.setVideoURI(mediaItem.pathUri)
        videoView.start()
        videoHandler.sendEmptyMessageDelayed(UPDATE_UI,500)
    }
    private fun initEvent(){
        videoPlay.setOnClickListener(View.OnClickListener {
            if (videoView.isPlaying) {
                videoPlay.setImageResource(R.drawable.play_btn_style)
                videoView.pause()
                videoHandler.removeMessages(UPDATE_UI)
            } else {
                videoPlay.setImageResource(R.drawable.pause_btn_style)
                videoView.start()
                videoHandler.sendEmptyMessageDelayed(UPDATE_UI, 500)
            }
        })

        videoView.setOnCompletionListener {
            MediaPlayer.OnCompletionListener {
                videoPlay.setImageResource(R.drawable.play_btn_style)
            }

        }

        playSeek.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                b: Boolean
            ) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                videoHandler.removeMessages(UPDATE_UI)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val progress = seekBar.progress
                videoView.seekTo(progress)
                videoHandler.sendEmptyMessage(UPDATE_UI)
            }
        })
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

    override fun onPause() {
        super.onPause()
        currentPosition = videoView.currentPosition
        videoView.pause()
    }

    override fun onResume() {

        if (currentPosition >= 0) {
            videoView.start()
            videoView.seekTo(currentPosition)
            currentPosition = -1
        }
        super.onResume()
    }
}