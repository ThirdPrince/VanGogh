package com.vangogh.media.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaPreviewAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.viewmodel.SelectMediaViewModel
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlinx.android.synthetic.main.activity_select_media.*
import kotlinx.android.synthetic.main.media_grid_top_bar.*
import kotlinx.android.synthetic.main.media_select_button.*
import kotlin.properties.Delegates

/**
 * @ClassName BaseSelectActivity
 * @Description media preview base UI
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
abstract class BaseSelectActivity : AppCompatActivity(), View.OnClickListener {



    companion object{
        const val TAG = "BaseSelectActivity"
    }
    lateinit var activity: BaseSelectActivity



    lateinit var selectMediaViewModel: SelectMediaViewModel

    var mediaPos: Int = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        VanGogh.selectMediaActivity.add(this)
        selectMediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                SelectMediaViewModel::class.java
            )
        selectMediaViewModel.lvMediaData.observe(this, Observer {
            VanGogh._lvMediaData.postValue(it)
            finishSelectMediaUi()
        })

    }

    protected fun getData() {
        mediaPos = intent!!.getIntExtra(GalleryActivity.MEDIA_POS, 0)
    }
    protected fun initSendMediaListener() {
        mediaLeftBack?.setOnClickListener(this)
        media_send?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mediaLeftBack -> finish()
            R.id.media_send -> {
                view_stub?.visibility = View.VISIBLE
                if(VanGogh.selectMediaList.isEmpty()){
                    VanGogh.selectMediaList.add(MediaPreviewUtil.mediaItemList!![mediaPos])
                }
                selectMediaViewModel.compressImage(VanGogh.selectMediaList)
            }
        }
    }


    protected fun updateTitle(){
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
    private fun finishSelectMediaUi(){
        VanGogh.selectMediaActivity.forEach {
            it.finish()
        }
    }



}