package com.example.vangogh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vangogh.adapter.GridMediaAdapter
import com.example.vangogh.itf.OnAddMediaListener
import com.vangogh.media.config.VanGogh
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.itf.OnAvatarResult
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.BaseSelectActivity
import com.vangogh.media.itf.OnMediaResult as OnMediaResult

/**
 * @ClassName MainActivity
 * @Description just test
 * @Author dhl
 * @Date 2021/1/26 10:25
 * @Version 1.0
 */
class MainActivity : AppCompatActivity(), OnAddMediaListener{

    private lateinit var gridMediaAdapter: GridMediaAdapter
    private var mediaList = mutableListOf<MediaItem>()
    private lateinit var rcyView: RecyclerView
    private lateinit var activity: MainActivity


    private lateinit var radioGp: RadioGroup
    private lateinit var mediaRb: RadioButton
    private lateinit var mediaNoGifRb: RadioButton
    private lateinit var imageRb: RadioButton
    private lateinit var imageNoGifRb: RadioButton


    private lateinit var gifRb: RadioButton

    private lateinit var videoRb: RadioButton

    private lateinit var videoMaxDurationRb: RadioButton

    private lateinit var forAvatar: RadioButton

    private lateinit var javaMedia:Button

    private lateinit var vanGogh: VanGogh


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        initView()
    }

    private fun initView() {
        rcyView = findViewById(R.id.rcy_view)
        radioGp = findViewById(R.id.radio_gp)
        mediaRb = findViewById(R.id.media_rb)
        mediaNoGifRb = findViewById(R.id.media_no_gif_rb)
        imageRb = findViewById(R.id.image_rb)
        imageNoGifRb = findViewById(R.id.image_no_gif_rb)
        gifRb = findViewById(R.id.gif_rb)
        videoRb = findViewById(R.id.video_rb)
        videoMaxDurationRb = findViewById(R.id.video_max_duration_rb)
        forAvatar = findViewById(R.id.forAvatar)
        javaMedia = findViewById(R.id.java_media)
        val layoutManager = GridLayoutManager(this, 4)
        rcyView.layoutManager = layoutManager
        rcyView.itemAnimator = DefaultItemAnimator()
        rcyView.addItemDecoration(GridSpacingItemDecoration(4, 5, false))
        gridMediaAdapter = GridMediaAdapter(this, mediaList)
        gridMediaAdapter.onAddMediaListener = activity
        rcyView.adapter = gridMediaAdapter

        javaMedia.setOnClickListener {
            val intent = Intent(this,MainActivityJava::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onAddMediaClick() {
        selectFilter()
        if( forAvatar.isChecked){
            vanGogh.startForAvatarResult(this,onAvatarResult = object :OnAvatarResult{
                override fun onResult(image: MediaItem) {
                    mediaList.clear()
                    mediaList.add(image)
                    gridMediaAdapter.notifyDataSetChanged()
                }

            })
        }else{
            vanGogh.startForResult(this,onMediaResult = object :OnMediaResult{
                override fun onResult(mediaItemList: List<MediaItem>) {
                    mediaList.clear()
                    mediaList.addAll(mediaItemList)
                    Log.e("Main","mediaList = ${mediaList.toString()}")
                    gridMediaAdapter.notifyDataSetChanged()
                }

            })
        }


    }

    private fun selectFilter() {
        vanGogh = VanGogh.getMedia()
        when {
            mediaRb.isChecked -> vanGogh = VanGogh.getMedia()

            mediaNoGifRb.isChecked -> vanGogh = VanGogh.getMedia(false)

            imageRb.isChecked -> vanGogh = VanGogh.onlyImage()

            imageNoGifRb.isChecked -> vanGogh = VanGogh.onlyImage(false)

            gifRb.isChecked -> vanGogh = VanGogh.onlyGif()

            videoRb.isChecked -> VanGogh.onlyVideo()

            videoMaxDurationRb.isChecked -> VanGogh.setVideoMaxDuration(10)


        }
    }


}