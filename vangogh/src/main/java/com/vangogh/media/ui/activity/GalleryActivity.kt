package com.vangogh.media.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.utils.MediaPreviewUtil
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlin.properties.Delegates

/**
 * @ClassName GalleryActivity
 * @Description media preview
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class GalleryActivity : AppCompatActivity() {

    companion object{


        /**
         * media index
         */
        const val MEDIA_POS = "mediaPos"

        fun actionStart(activity: AppCompatActivity,mediaPos :Int){
            var intent = Intent(activity,GalleryActivity::class.java).apply {
                putExtra(MEDIA_POS,mediaPos)
            }
            activity.startActivity(intent)

        }
    }

     lateinit var activity: GalleryActivity

     var mediaPos :Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        activity = this
        getData()
        view_pager2.apply {
            //offscreenPageLimit = 1
            val recyclerView = getChildAt(0) as RecyclerView
            adapter = MediaPreviewAdapter(activity,MediaPreviewUtil.mediaItemList!!)
            setCurrentItem(mediaPos,false)
        }
    }

    private fun getData(){
        mediaPos = intent!!.getIntExtra(MEDIA_POS,0)
    }
}