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
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.extend.toast
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.ImageUtils
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.view.AnimateCheckBox

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

        const val IMAGE_ORIGINAL = "imageOriginal"

        const val MEDIA_PREVIEW_SELECT = "mediaPreviewSelect"

        const val REQUEST_CODE = 1024

        fun actionStart(
            activity: AppCompatActivity,
            mediaPos: Int,
            imageOriginal: Boolean,
            isPreviewSelectMedia: Boolean
        ) {
            var intent = Intent(activity, GalleryActivity::class.java).apply {
                putExtra(MEDIA_POS, mediaPos)
                putExtra(IMAGE_ORIGINAL, imageOriginal)
                putExtra(MEDIA_PREVIEW_SELECT, isPreviewSelectMedia)
            }
            activity.startActivityForResult(intent, REQUEST_CODE)

        }


    }


    private val topBarRoot by lazy { findViewById<RelativeLayout>(R.id.top_bar_root) }

    private val mediaIndexTv by lazy { findViewById<TextView>(R.id.media_index_tv) }

    private val viewPager2 by lazy { findViewById<ViewPager2>(R.id.view_pager2) }

    private val checkbox by lazy { findViewById<AnimateCheckBox>(R.id.checkbox) }

    private var previewMediaList = mutableListOf<MediaItem>()


    private var currentMedia: MediaItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initListener()
        viewPager2.apply {
            offscreenPageLimit = 1
            previewMediaList = if (mediaPreviewSelect) {
                VanGogh.selectMediaList
            } else {
                MediaPreviewUtil.currentMediaList!!
            }
            adapter = MediaPreviewAdapter(activity, previewMediaList)
            setCurrentItem(mediaPos, false)
            setMediaIndex()
            setSelectMediaState()
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (!mediaPreviewSelect){

                    }
                    mediaPos = position
                    setMediaIndex()
                    setSelectMediaState()
                }

            })
        }
    }

    override fun contentLayout(): Int {
        return R.layout.activity_gallery
    }

    override fun backPress() {
        val intentBack = Intent().apply {
            putExtra(GalleryActivity.IMAGE_ORIGINAL, cbOriginal.isChecked)
        }
        setResult(Activity.RESULT_CANCELED, intentBack)
        finish()
    }


        private fun initListener() {
            topBarRoot.setOnClickListener {
                finish()
            }
            checkbox.setOnCheckedChangeListener(object : AnimateCheckBox.OnCheckedChangeListener {
                @SuppressLint("StringFormatMatches")
                override fun onCheckedChanged(checkBox: AnimateCheckBox, isChecked: Boolean) {
                    if (isChecked) {
                        imageOriginal = true
                        if (VanGogh.selectMediaList.size > VanGoghConst.MAX_MEDIA - 1) {
                            toast(
                                getString(
                                    R.string.picture_message_max_num,
                                    VanGoghConst.MAX_MEDIA
                                )
                            )
                            checkbox.isChecked = false
                            return
                        }
                        if (!VanGogh.selectMediaList.contains(currentMedia)) {
                            if (cbOriginal.isChecked) {
                                currentMedia!!.isOriginal = true
                            }
                            VanGogh.selectMediaList.add(currentMedia!!)
                        }

                    } else {
                        imageOriginal = false
                        VanGogh.selectMediaList.remove(currentMedia!!)
                    }
                    updateTitle()
                }

            })
        }

        private fun setMediaIndex() {
            currentMedia = previewMediaList!![mediaPos]
           // Log.e(TAG, (currentMedia!!.width ===0).toString())
            val damage = currentMedia!!.isImage() && currentMedia!!.width === 0 && !ImageUtils.isImage(currentMedia!!.originalPath)
            Log.e(TAG,damage.toString())
            mediaIndexTv.text = "${mediaPos + 1} / ${previewMediaList!!.size}"
            if(currentMedia!!.isImage()){
                cbOriginal.visibility = View.VISIBLE
            }else{
                cbOriginal.visibility = View.GONE
            }


        }

        private fun setSelectMediaState() {
            checkbox.setChecked(
                VanGogh.selectMediaList.contains(
                    previewMediaList?.get(
                        mediaPos
                    )
                ), false
            )
        }




    }