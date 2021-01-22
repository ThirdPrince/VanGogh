package com.vangogh.media.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.utils.MediaPreviewUtil
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.media_select_button.*
import kotlin.properties.Delegates

/**
 * @ClassName GalleryActivity
 * @Description media preview
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class GalleryActivity : BaseSelectActivity() {

    companion object {

        /**
         * media index
         */
        const val MEDIA_POS = "mediaPos"

        fun actionStart(activity: AppCompatActivity, mediaPos: Int) {
            var intent = Intent(activity, GalleryActivity::class.java).apply {
                putExtra(MEDIA_POS, mediaPos)
            }
            activity.startActivity(intent)

        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        initSendMediaListener()
        getData()
        view_pager2.apply {
            offscreenPageLimit = 1
            adapter = MediaPreviewAdapter(activity, MediaPreviewUtil.mediaItemList!!)
            setCurrentItem(mediaPos, false)
            registerOnPageChangeCallback(object : OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mediaPos = position
                }

            })
        }
        media_send.isEnabled = true
    }


}