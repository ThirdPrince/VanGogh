package com.vangogh.media.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.media.vangogh.R
import com.vangogh.media.config.VanGogh
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.viewmodel.CompressMediaViewModel
import kotlinx.android.synthetic.main.activity_select_media.*
import kotlinx.android.synthetic.main.media_grid_top_bar.*
import kotlinx.android.synthetic.main.media_select_button.*

/**
 * @ClassName BaseSelectActivity
 * @Description media preview base UI
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
abstract class BaseSelectActivity : AppCompatActivity(), View.OnClickListener {


    companion object {
        const val TAG = "BaseSelectActivity"
    }



    lateinit var activity: BaseSelectActivity


    lateinit var selectMediaViewModel: CompressMediaViewModel

    var mediaPos: Int = 0

    protected var imageOriginal: Boolean  = false

    protected var mediaPreviewSelect: Boolean  = false


    /**
     * activity back
     */

    private val mediaLeftBack: ImageView by lazy {
        findViewById<ImageView>(R.id.mediaLeftBack)
    }

    /**
     * selectMedia complete
     */
    private val mediaSend: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.media_send)
    }

    /**
     *  check box for image isOriginal
     */
    protected val cbOriginal: AppCompatCheckBox by lazy {
        findViewById<AppCompatCheckBox>(R.id.cb_original)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.picture_color_grey)
        }
        setContentView(contentLayout())
        initSendMediaListener()
        initOriginalCheck()
        activity = this
        VanGogh.selectMediaActivity.add(this)
        selectMediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                CompressMediaViewModel::class.java
            )
        selectMediaViewModel.lvMediaData.observe(this, Observer {
            VanGogh._lvMediaData.postValue(it)
            finishSelectMediaUi()
        })

    }

    protected fun getData() {
        mediaPos = intent!!.getIntExtra(GalleryActivity.MEDIA_POS, 0)
        imageOriginal = intent!!.getBooleanExtra(GalleryActivity.IMAGE_ORIGINAL,false)
        mediaPreviewSelect = intent!!.getBooleanExtra(GalleryActivity.MEDIA_PREVIEW_SELECT,false)
        cbOriginal.isChecked = imageOriginal
    }

    private fun initSendMediaListener() {
        mediaLeftBack?.setOnClickListener(this)
        mediaSend.setOnClickListener(this)
    }

    /**
     * image is OriginalCheck
     */
    private fun initOriginalCheck() {
        cbOriginal?.setOnCheckedChangeListener { buttonView, isChecked ->

            VanGogh.selectMediaList.forEach {
                it.isOriginal = isChecked
            }

        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mediaLeftBack -> backPress()
            R.id.media_send -> {
                view_stub?.visibility = View.VISIBLE
                if (VanGogh.selectMediaList.isEmpty()) {
                    VanGogh.selectMediaList.add(MediaPreviewUtil.currentMediaList!![mediaPos])
                }
                selectMediaViewModel.compressImage(VanGogh.selectMediaList)
            }
        }
    }


    protected fun updateTitle() {
        if (VanGogh.selectMediaList.size > 0) {
            media_send.isEnabled = true
            media_send.text = getString(R.string.media_send_num, VanGogh.selectMediaList.size, 9)
        } else {
            media_send.isEnabled = false
            media_send.text = resources.getString(R.string.media_send_not_enable)
        }
    }

    /**
     * finish mediaUI
     */
    private fun finishSelectMediaUi() {
        VanGogh.selectMediaActivity.forEach {
            it.finish()
        }
    }

    /**
     * @return 布局文件
     */
    @LayoutRes
    protected abstract fun contentLayout(): Int



    protected abstract fun backPress()


    override fun onDestroy() {
        super.onDestroy()
        VanGogh.selectMediaActivity.remove(this)
    }

    override fun onBackPressed() {
        backPress()
    }

}