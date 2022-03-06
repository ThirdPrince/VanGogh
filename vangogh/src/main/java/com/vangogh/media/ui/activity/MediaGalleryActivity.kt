package com.vangogh.media.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.core.log.EasyLog
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.extend.toast
import com.vangogh.media.models.MediaItem
import com.vangogh.media.picEdit.dialog.EditorFinishCallback
import com.vangogh.media.picEdit.dialog.PictureEditorDialog
import com.vangogh.media.ui.dialog.LoadingDialog
import com.vangogh.media.utils.ImageUtils
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.utils.SystemBar
import com.vangogh.media.view.AnimateCheckBox
import com.vangogh.media.viewmodel.CompressMediaViewModel

/**
 * Created by Jaeger on 16/2/14.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
class MediaGalleryActivity : AppCompatActivity() {


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
            var intent = Intent(activity, MediaGalleryActivity::class.java).apply {
                putExtra(MEDIA_POS, mediaPos)
                putExtra(IMAGE_ORIGINAL, imageOriginal)
                putExtra(MEDIA_PREVIEW_SELECT, isPreviewSelectMedia)
            }
            activity.startActivityForResult(intent, REQUEST_CODE)

        }


    }

    private val topBarRoot by lazy { findViewById<RelativeLayout>(R.id.top_bar_root) }

    private val mediaIndexTv by lazy { findViewById<TextView>(R.id.media_index_tv) }


    /**
     * activity back
     */

    private val mediaLeftBack: ImageView by lazy {
        findViewById(R.id.mediaLeftBack)
    }

    /**
     * selectMedia complete
     */
    private val mediaSend: AppCompatButton by lazy {
        findViewById(R.id.media_send)
    }

    /**
     *  check box for image isOriginal
     */
    private val cbOriginal: AppCompatCheckBox by lazy {
        findViewById(R.id.cb_original)
    }

    private val viewPager2 by lazy { findViewById<ViewPager2>(R.id.view_pager2) }

    private val checkbox by lazy { findViewById<AnimateCheckBox>(R.id.checkbox) }

    private val picEdit by lazy { findViewById<AppCompatTextView>(R.id.pic_editor) }

    private var previewMediaList = mutableListOf<MediaItem>()


    private var currentMedia: MediaItem? = null

    private var imageOriginal: Boolean = false

    protected var mediaPreviewSelect: Boolean = false

    var mediaPos: Int = 0

    lateinit var compressMediaViewModel: CompressMediaViewModel

    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.media_activity_gallery)
        SystemBar.invasionStatusBar(this)
        SystemBar.invasionNavigationBar(this)
        SystemBar.setStatusBarColor(this, Color.TRANSPARENT)
        SystemBar.setNavigationBarColor(this,  ContextCompat.getColor(this, R.color.albumSheetBottom))
        initData()
        initListener()
        compressMediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                CompressMediaViewModel::class.java
            )
        compressMediaViewModel.lvMediaData.observe(this, Observer {

            VanGogh.mOnMediaResult.onResult(it)

            dismissDialog()
            finishSelectMediaUi()
        })
        viewPager2.apply {
            offscreenPageLimit = 1
            previewMediaList = if (mediaPreviewSelect) {
                VanGogh.selectMediaList
            } else {
                MediaPreviewUtil.currentMediaList!!
            }
            adapter = MediaPreviewAdapter(this@MediaGalleryActivity, previewMediaList)
            setCurrentItem(mediaPos, false)
            setMediaIndex()
            setSelectMediaState()
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (!mediaPreviewSelect) {

                    }
                    mediaPos = position
                    setMediaIndex()
                    setSelectMediaState()
                }

            })
        }

    }

    private fun initData(){
        mediaPos = intent!!.getIntExtra(GalleryActivity.MEDIA_POS, 0)
        imageOriginal = intent!!.getBooleanExtra(MediaGalleryActivity.IMAGE_ORIGINAL, false)
        mediaPreviewSelect = intent!!.getBooleanExtra(MediaGalleryActivity.MEDIA_PREVIEW_SELECT, false)
    }

    private fun initListener() {
        topBarRoot.setOnClickListener {
            finish()
        }

        mediaSend.setOnClickListener {
            if (VanGogh.selectMediaList.isEmpty()) {
                VanGogh.selectMediaList.add(MediaPreviewUtil.currentMediaList!![mediaPos])
            }
            showDialog()
            compressMediaViewModel.compressImage(VanGogh.selectMediaList)
        }

        picEdit.setOnClickListener {
            currentMedia?.path?.let { it1 ->
                PictureEditorDialog.newInstance()
                    .setBitmapPath(it1)
                    .setEditorFinishCallback(object : EditorFinishCallback {
                        override fun onFinish(path: String) {
                            EasyLog.e(BaseSelectActivity.TAG, "path = $path")
                            checkbox.isChecked = true
                            updateTitle()
                            currentMedia?.originalPath = path
                            viewPager2.adapter?.notifyDataSetChanged()

                        }
                    })
                    .show(supportFragmentManager)
            }
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
                        if (cbOriginal?.isChecked == true) {
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
        if (!currentMedia?.isImage()!!) {
            picEdit.visibility = View.GONE
        }
        preCompressImage()
        val damage = currentMedia!!.isImage() && currentMedia!!.width === 0 && !ImageUtils.isImage(
            currentMedia!!.originalPath
        )
        Log.e(BaseSelectActivity.TAG, damage.toString())
        mediaIndexTv.text = "${mediaPos + 1} / ${previewMediaList!!.size}"
        if (currentMedia!!.isImage()) {
            cbOriginal?.visibility = View.VISIBLE
        } else {
            cbOriginal?.visibility = View.GONE
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

    private fun preCompressImage() {
        val mediaList = mutableListOf<MediaItem>()
        mediaList.add(currentMedia!!)
        compressMediaViewModel.compressImage(mediaList, true)
    }

    private fun updateTitle() {
        mediaSend?.isEnabled = true
        if (VanGogh.selectMediaList.size > 0) {
            mediaSend?.isEnabled = true
            when (VanGoghConst.MEDIA_TITLE) {
                VanGoghConst.MediaTitle.MediaComplete -> mediaSend?.text = getString(
                    R.string.media_complete_num,
                    VanGogh.selectMediaList.size,
                    VanGoghConst.MAX_MEDIA
                )
                VanGoghConst.MediaTitle.MediaSend -> mediaSend?.text = getString(
                    R.string.media_send_num,
                    VanGogh.selectMediaList.size,
                    VanGoghConst.MAX_MEDIA
                )
            }
        } else {
            when (VanGoghConst.MEDIA_TITLE) {
                VanGoghConst.MediaTitle.MediaComplete -> mediaSend?.text =
                    resources.getString(R.string.media_complete)
                VanGoghConst.MediaTitle.MediaSend -> mediaSend?.text =
                    resources.getString(R.string.media_send_not_enable)

            }
        }

    }

    private fun showDialog() {
        loadingDialog.show()
    }

    private fun dismissDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    /**
     * finish mediaUI
     */
    private fun finishSelectMediaUi() {
        finish()
        VanGogh.selectMediaActivity.forEach {
            it.finish()
            if (it is SelectMediaActivity) {
                overridePendingTransition(0, R.anim.picture_anim_down_out)
            }
        }
    }


}