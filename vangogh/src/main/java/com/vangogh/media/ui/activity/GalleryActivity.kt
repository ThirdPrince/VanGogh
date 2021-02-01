package com.vangogh.media.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.extend.toast
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.view.AnimateCheckBox
import kotlinx.android.synthetic.main.media_select_button.*

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

        const val REQUEST_CODE = 1024

        fun actionStart(activity: AppCompatActivity, mediaPos: Int) {
            var intent = Intent(activity, GalleryActivity::class.java).apply {
                putExtra(MEDIA_POS, mediaPos)
            }
            activity.startActivityForResult(intent, REQUEST_CODE)

        }
    }

    private val topBarRoot by lazy { findViewById<RelativeLayout>(R.id.top_bar_root) }

    private val mediaIndexTv by lazy { findViewById<TextView>(R.id.media_index_tv) }

    private val viewPager2 by lazy { findViewById<ViewPager2>(R.id.view_pager2) }

    private val checkbox by lazy { findViewById<AnimateCheckBox>(R.id.checkbox) }

    private var currentMedia: MediaItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        initSendMediaListener()
        getData()
        initListener()
        viewPager2.apply {
            offscreenPageLimit = 1
            adapter = MediaPreviewAdapter(activity, MediaPreviewUtil.currentMediaList!!)
            setCurrentItem(mediaPos, false)
            Log.e(TAG,"media="+MediaPreviewUtil.currentMediaList!![mediaPos])
            setMediaIndex()
            setSelectMediaState()
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mediaPos = position
                    setMediaIndex()
                    setSelectMediaState()
                }

            })
        }
        media_send.isEnabled = true
    }

    private fun initListener() {
        topBarRoot.setOnClickListener {
            finish()
        }
        checkbox.setOnCheckedChangeListener(object : AnimateCheckBox.OnCheckedChangeListener {
            @SuppressLint("StringFormatMatches")
            override fun onCheckedChanged(checkBox: AnimateCheckBox, isChecked: Boolean) {
                if (isChecked) {
                    if(VanGogh.selectMediaList.size > VanGoghConst.MAX_MEDIA-1){
                        toast(getString(R.string.picture_message_max_num, VanGoghConst.MAX_MEDIA))
                        checkbox.isChecked = false
                        return
                    }
                    if (!VanGogh.selectMediaList.contains(currentMedia)) {
                        VanGogh.selectMediaList.add(currentMedia!!)
                    }

                } else {
                    VanGogh.selectMediaList.remove(currentMedia!!)
                }
                updateTitle()
            }

        })
    }

    private fun setMediaIndex() {
        currentMedia = MediaPreviewUtil.currentMediaList!![mediaPos]
        mediaIndexTv.text = "${mediaPos + 1} / ${MediaPreviewUtil.currentMediaList!!.size}"
    }

    private fun setSelectMediaState() {
        checkbox.setChecked(
            VanGogh.selectMediaList.contains(
                MediaPreviewUtil.currentMediaList?.get(
                    mediaPos
                )
            ), false
        )
        //updateTitle()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent()
        setResult(Activity.RESULT_CANCELED, intent)
        finish()
    }


}