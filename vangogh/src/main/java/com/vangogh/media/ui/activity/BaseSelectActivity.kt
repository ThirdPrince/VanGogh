package com.vangogh.media.ui.activity

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.media.vangogh.R
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.itf.OnMediaResult
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.dialog.LoadingDialog
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.viewmodel.CompressMediaViewModel
import kotlinx.android.synthetic.main.media_select_button.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


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
    val uiScope  =  CoroutineScope(Dispatchers.Main)


    lateinit var activity: BaseSelectActivity


    lateinit var compressMediaViewModel: CompressMediaViewModel

    var mediaPos: Int = 0

    protected var imageOriginal: Boolean  = false

    protected var mediaPreviewSelect: Boolean  = false


    protected var isAvatar :Boolean = false

    /**
     * 已选中的media
     */
    protected var selectedList = mutableListOf<MediaItem>()

    /**
     * activity back
     */

    private val mediaLeftBack: ImageView by lazy {
        findViewById<ImageView>(R.id.mediaLeftBack)
    }

    /**
     * selectMedia complete
     */
    protected val mediaSend: AppCompatButton by lazy {
        findViewById<AppCompatButton>(R.id.media_send)
    }

    /**
     *  check box for image isOriginal
     */
    protected val cbOriginal: AppCompatCheckBox by lazy {
        findViewById<AppCompatCheckBox>(R.id.cb_original)
    }

    private lateinit var loadingDialog:LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = ContextCompat.getColor(this, R.color.media_color_grey)
        }
        setContentView(contentLayout())
        initLoadingDialog()
        getData()
        initSendMediaListener()
        initOriginalCheck()
        updateTitle()
        activity = this
        VanGogh.selectMediaActivity.add(this)
        compressMediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                CompressMediaViewModel::class.java
            )
        compressMediaViewModel.lvMediaData.observe(this, Observer {
            if (isAvatar) {
                VanGogh.mOnAvatarResult.onResult(it[0])
            } else {
                VanGogh.mOnMediaResult.onResult(it)
            }
            VanGoghConst.reset()
            dismissDialog()
            finishSelectMediaUi()
        })

    }

    private fun getData() {
        mediaPos = intent!!.getIntExtra(GalleryActivity.MEDIA_POS, 0)
        isAvatar = intent!!.getBooleanExtra(SelectMediaActivity.IS_AVATAR, false)
        //selectedList = intent!!.getParcelableArrayListExtra(SelectMediaActivity.SELECTED_LIST)
        imageOriginal = intent!!.getBooleanExtra(GalleryActivity.IMAGE_ORIGINAL, false)
        mediaPreviewSelect = intent!!.getBooleanExtra(GalleryActivity.MEDIA_PREVIEW_SELECT, false)
        cbOriginal?.isChecked = imageOriginal
        if(isAvatar){
            cbOriginal?.visibility = View.GONE
            mediaSend.visibility = View.GONE
        }else{
            cbOriginal?.visibility = View.VISIBLE
            mediaSend.visibility = View.VISIBLE
        }
    }

    private fun initSendMediaListener() {
        mediaLeftBack?.setOnClickListener(this)
        mediaSend.setOnClickListener(this)
    }

    private fun initLoadingDialog(){
        loadingDialog = LoadingDialog(this)
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
                if (VanGogh.selectMediaList.isEmpty()) {
                    VanGogh.selectMediaList.add(MediaPreviewUtil.currentMediaList!![mediaPos])
                }
                showDialog()
                compressMediaViewModel.compressImage(VanGogh.selectMediaList)
            }
        }
    }


    protected fun updateTitle() {
        if (VanGogh.selectMediaList.size > 0) {
            media_send.isEnabled = true
            when(VanGoghConst.MEDIA_TITLE){
                VanGoghConst.MediaTitle.MediaComplete -> media_send.text = getString(R.string.media_complete_num, VanGogh.selectMediaList.size, 9)
                VanGoghConst.MediaTitle.MediaSend -> media_send.text = getString(R.string.media_send_num, VanGogh.selectMediaList.size, 9)
            }
        } else {
            media_send.isEnabled = false
            when(VanGoghConst.MEDIA_TITLE){
                VanGoghConst.MediaTitle.MediaComplete ->media_send.text = resources.getString(R.string.media_complete)
                VanGoghConst.MediaTitle.MediaSend ->   media_send.text = resources.getString(R.string.media_send_not_enable)

            }
        }

    }

    /**
     * finish mediaUI
     */
    private fun finishSelectMediaUi() {
        VanGogh.selectMediaActivity.forEach {
            it.finish()
            if(it is SelectMediaActivity){
                overridePendingTransition(0,R.anim.picture_anim_down_out)
            }
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

    protected fun showDialog(){
        loadingDialog.show()
    }

    protected fun dismissDialog(){
        if(loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

}