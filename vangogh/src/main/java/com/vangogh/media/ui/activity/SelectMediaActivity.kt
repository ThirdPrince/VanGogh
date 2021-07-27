package com.vangogh.media.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaGridItemAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.extend.toast
import com.vangogh.media.itf.OnCameraClickListener
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaDir
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.CameraManager
import com.vangogh.media.utils.ImageUtils
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.utils.MediaTimeUtils
import com.vangogh.media.view.MediaDirPopWindow
import com.vangogh.media.viewmodel.MediaViewModel
import kotlinx.android.synthetic.main.activity_select_media.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException


/**
 * @ClassName SelectMediaActivity
 * @Description  MediaUI
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class SelectMediaActivity : BaseSelectActivity(), View.OnClickListener, OnMediaItemClickListener,
    OnItemCheckListener, OnCameraClickListener {

    companion object {

        const val IS_AVATAR = "isAvatar"
        //const val SELECTED_LIST = "SelectedList"

        fun actionStart(activity: FragmentActivity, isAvatar: Boolean) {
            var intent = Intent(
                activity,
                SelectMediaActivity::class.java
            ).apply {
                putExtra(IS_AVATAR, isAvatar)
                //putExtra(SELECTED_LIST,selectedList)
            }
            activity.startActivity(intent)
            activity.overridePendingTransition(
                R.anim.picture_anim_up_in,
                R.anim.picture_anim_down_out
            )

        }
    }


    private lateinit var rcyView: RecyclerView


    private lateinit var gridLayoutManager: GridLayoutManager

    /**
     * query media ViewModel
     */
    private lateinit var mediaViewModel: MediaViewModel

    /**
     * media show
     */

    private lateinit var mediaItemAdapter: MediaGridItemAdapter

    private lateinit var mediaTitleLay: LinearLayout

    private lateinit var mediaTitle: TextView

    private lateinit var titleViewBg: View

    private lateinit var ivArrow: ImageView

    private var cameraManager: CameraManager? = null

    /**
     * preview media
     */
    private val mediaPreview: TextView by lazy {
        findViewById<TextView>(R.id.media_preview_tv)
    }

    private val tvImageTime: TextView by lazy {
        findViewById<TextView>(R.id.tv_image_time)
    }


    /**
     * media dir list
     */

    private lateinit var mediaDirList: List<MediaDir>

    /**
     * MediaDir List
     */
    private var popWindow: MediaDirPopWindow? = null

    /**
     * permissions
     */
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initMediaDirPop()
        initScrollEvent()
        mediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                MediaViewModel::class.java
            )

        val checkPermission = this?.let { ActivityCompat.checkSelfPermission(it, permissions[0]) }
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            //这是系统的方法
            requestPermissions(permissions, 0)
        } else {
            showDialog()
            mediaViewModel.getMedia(null)
        }
        mediaViewModel.lvMediaData.observe(this, Observer {
            dismissDialog()
            if(it.size == MediaPreviewUtil.currentMediaList.size){
                return@Observer
            }
            MediaPreviewUtil.currentMediaList.clear()
            MediaPreviewUtil.currentMediaList.addAll(it)
            updateTitle()
            mediaItemAdapter.notifyDataSetChanged()


        })
        mediaViewModel.lvDataChanged.observe(this, Observer {

            mediaViewModel.getMedia(null)

        })
        mediaViewModel.lvMediaDirData.observe(this, Observer {
            mediaDirList = it
            if (popWindow != null && popWindow?.dirCheckPosition != 0) {
                var mediaItemList = mediaDirList[popWindow?.dirCheckPosition!!].medias
                MediaPreviewUtil.currentMediaList.clear()
                MediaPreviewUtil.currentMediaList.addAll(mediaItemList)
                mediaItemAdapter.notifyDataSetChanged()
            }

        })


    }

    override fun contentLayout(): Int {
        return R.layout.activity_select_media
    }

    override fun backPress() {
        finish()
        overridePendingTransition(0, R.anim.picture_anim_down_out)
    }


    @SuppressLint("ResourceAsColor")
    private fun initView() {
        mediaTitleLay = findViewById(R.id.media_title_lay)
        mediaTitle = findViewById(R.id.media_title)
        titleViewBg = findViewById(R.id.titleViewBg)
        ivArrow = findViewById(R.id.ivArrow)
        ivArrow.animate().rotationBy(90f)
        rcyView = findViewById(R.id.rcy_view)
        gridLayoutManager = GridLayoutManager(this, VanGoghConst.GRID_SPAN_CONT)
        rcyView.layoutManager = gridLayoutManager
        rcyView.itemAnimator = DefaultItemAnimator()
        rcyView.addItemDecoration(GridSpacingItemDecoration(4, 5, false))
        mediaItemAdapter = MediaGridItemAdapter(this, MediaPreviewUtil.currentMediaList, isAvatar)
        mediaItemAdapter.setHasStableIds(true);
        rcy_view.adapter = mediaItemAdapter
        mediaItemAdapter.onMediaItemClickListener = this
        mediaItemAdapter.onItemCheckListener = this
        mediaItemAdapter.onCameraClickListener = this
        when (VanGoghConst.MEDIA_TYPE) {
            VanGoghConst.MediaType.MediaAll -> mediaTitle.text = getString(R.string.media_title_str)
            VanGoghConst.MediaType.MediaOnlyImage -> mediaTitle.text =
                getString(R.string.image_title_str)
            VanGoghConst.MediaType.MediaOnlyGif -> mediaTitle.text =
                getString(R.string.gif_title_str)
            VanGoghConst.MediaType.MediaOnlyVideo -> mediaTitle.text =
                getString(R.string.video_title_str)
        }

    }

    private fun initScrollEvent() {
        rcyView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        tvImageTime.animate().alpha(0f).start()
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> updateImageTime()
                }


            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun initMediaDirPop() {
        mediaTitleLay.setOnClickListener {
            if (popWindow == null) {
                popWindow = MediaDirPopWindow(this, mediaDirList, ivArrow)
            }
            popWindow?.showAsDropDown(titleViewBg)
            popWindow?.setOnMediaItemClickListener(object : OnMediaItemClickListener {
                override fun onItemClick(view: View?, position: Int) {
                    if (popWindow?.dirCheckPosition !== position) {
                        mediaTitle.text = popWindow?.mediaDirAdapter!!.items[position].name
                        var mediaItemList = mediaDirList[position].medias
                        popWindow?.dirCheckPosition = position
                        MediaPreviewUtil.currentMediaList.clear()
                        MediaPreviewUtil.currentMediaList.addAll(mediaItemList)
                        val lastVisible: Int = gridLayoutManager.findLastVisibleItemPosition()
                        if (lastVisible > 25) {
                            rcyView.scrollToPosition(0)
                        }
                        mediaItemAdapter.notifyDataSetChanged()
                    }
                    popWindow?.dismiss()
                }
            })

        }

        mediaPreview.setOnClickListener {
            GalleryActivity.actionStart(this, 0, cbOriginal.isChecked, true)
        }
    }

    private fun refreshMedia() {
        mediaItemAdapter.selectMediaList = VanGogh.selectMediaList
        updateTitle()
        mediaItemAdapter.notifyDataSetChanged()
        updateMediaPreview()
    }


    /**
     * camera click
     */
    override fun onCameraClick(view: View?) {
        cameraManager = CameraManager(this)
        try {
            uiScope.launch {
                val cameraIntent = withContext(Dispatchers.IO) {
                    cameraManager?.cameraIntent()
                }
                if (cameraIntent != null)
                    startActivityForResult(cameraIntent, CameraManager.REQUEST_CAMERA)
                else
                    toast(getString(R.string.no_camera_exists))

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    /**
     * item click
     */
    override fun onItemClick(view: View?, position: Int) {
        val realPosition = if (VanGoghConst.CAMERA_ENABLE) position - 1 else position
        if (isAvatar) {
            AvatarActivity.actionStart(this, realPosition, isAvatar)
        } else {
            GalleryActivity.actionStart(this, realPosition, cbOriginal.isChecked, false)
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun onItemCheckClick(view: View?, position: Int, isChecked: Boolean) {

        var mediaItem = mediaItemAdapter.getMedia(position)
        if (VanGogh.selectMediaList.contains(mediaItem)) {
            VanGogh.selectMediaList.remove(mediaItem)
            mediaItemAdapter.notifyDataSetChanged()
        } else {
            if (VanGogh.selectMediaList.size > VanGoghConst.MAX_MEDIA - 1) {
                toast(getString(R.string.picture_message_max_num, VanGoghConst.MAX_MEDIA))
                mediaItemAdapter.notifyItemChanged(position)
                return
            }
            if (cbOriginal.isChecked) {
                mediaItem.isOriginal = true
            }
            VanGogh.selectMediaList.add(mediaItem)
            mediaItemAdapter.notifyItemChanged(position)
        }
        mediaItemAdapter.selectMediaList = VanGogh.selectMediaList
        updateTitle()
        updateMediaPreview()
    }

    @SuppressLint("StringFormatMatches")
    private fun updateMediaPreview() {
        if (VanGogh.selectMediaList.size > 0) {
            mediaPreview.isEnabled = true
            mediaPreview.text = getString(R.string.media_preview_num, VanGogh.selectMediaList.size)
        } else {
            mediaPreview.isEnabled = false
            mediaPreview.text = getString(R.string.media_preview)
        }
    }


    /**
     * 更新时间
     */
    private fun updateImageTime() {
        val position: Int = gridLayoutManager.findFirstVisibleItemPosition()
        if (position != RecyclerView.NO_POSITION) {
            val mediaItem: MediaItem = mediaItemAdapter.items[position]
            if (mediaItem != null) {
                if (tvImageTime.visibility != View.VISIBLE) {
                    tvImageTime.visibility = View.VISIBLE
                }
                val time: String = MediaTimeUtils.getMediaTime(mediaItem.dataToken)
                tvImageTime.text = time
                tvImageTime.animate().alpha(1f).start()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GalleryActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    if (data != null) {
                        cbOriginal.isChecked =
                            data.getBooleanExtra(GalleryActivity.IMAGE_ORIGINAL, false)
                        refreshMedia()
                    }
                }
            }

            CameraManager.REQUEST_CAMERA -> {
                if ((resultCode == Activity.RESULT_OK)) {
                    val widthAndHeight = ImageUtils.getImageSize(cameraManager?.cameraRealPath)
                    val size = File(cameraManager?.cameraRealPath).length()
                    val cameraItem = MediaItem()
                    cameraItem.width = widthAndHeight[0]
                    cameraItem.height = widthAndHeight[1]
                    cameraItem.path = cameraManager?.cameraRealPath
                    cameraItem.pathUri = cameraManager?.cameraPathUri
                    cameraItem.size = size
                    cameraItem.mineType = "image/jpeg"
                    cameraItem.dataToken = System.currentTimeMillis()
                    MediaPreviewUtil.currentMediaList.add(0,cameraItem)
                    VanGogh.selectMediaList.add(cameraItem)
                    mediaItemAdapter.notifyDataSetChanged()
                    updateTitle()
                    sendBroadcast(
                        Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(File(cameraManager?.cameraRealPath))
                        )
                    )
                }

            }


        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showDialog()
            mediaViewModel.getMedia(null)
        }
    }


}