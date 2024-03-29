package com.vangogh.media.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.log.EasyLog
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaGridItemAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.extend.toast
import com.vangogh.media.fragment.CameraFragment
import com.vangogh.media.itf.*
import com.vangogh.media.life.VanGoghLifeObserver
import com.vangogh.media.models.MediaDir
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.*
import com.vangogh.media.utils.MediaPreviewUtil.currentMediaList
import com.vangogh.media.view.MediaDirPopWindow
import com.vangogh.media.viewmodel.CompressMediaViewModel
import com.vangogh.media.viewmodel.MediaViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.size
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

const val STORAGE_REQUEST = 1024
const val CAMERA_REQUEST = 1025

class SelectMediaActivity : BaseSelectActivity(), View.OnClickListener, OnMediaItemClickListener,
    OnItemCheckListener, OnCameraClickListener {

    

    companion object {

        const val IS_AVATAR = "isAvatar"
        const val IS_CAMERA = "isCamera"


        //const val SELECTED_LIST = "SelectedList"

        fun actionStart(
            activity: FragmentActivity,  onMediaResult: OnMediaResult,isAvatar: Boolean, isCamera: Boolean = false,

        ) {
            var intent = Intent(
                activity,
                SelectMediaActivity::class.java
            ).apply {
                putExtra(IS_AVATAR, isAvatar)
                putExtra(IS_CAMERA, isCamera)
                //putExtra(SELECTED_LIST,selectedList)
            }
            mOnMediaResult = onMediaResult
            activity.startActivity(intent)
            activity.overridePendingTransition(
                R.anim.picture_anim_up_in,
                R.anim.picture_anim_down_out
            )

        }

        fun actionStart(
            activity: FragmentActivity, onAvatarResult: OnAvatarResult, isAvatar: Boolean, isCamera: Boolean = false,

        ) {
            var intent = Intent(
                activity,
                SelectMediaActivity::class.java
            ).apply {
                putExtra(IS_AVATAR, isAvatar)
                putExtra(IS_CAMERA, isCamera)
                //putExtra(SELECTED_LIST,selectedList)
            }
            mOnAvatarResult = onAvatarResult
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
    private val permissionCamera = arrayOf(Manifest.permission.CAMERA)


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isCamera) {
            if (!PermissionUtils.checkSelfPermission(this, permissionCamera[0])) {
                requestPermissions(permissionCamera, CAMERA_REQUEST)
            } else {
                openCamera()
            }
            return

        }
        initView()
        initMediaDirPop()
        initScrollEvent()
        lifecycle.addObserver(VanGoghLifeObserver())

        mediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                MediaViewModel::class.java
            )
        if (!PermissionUtils.checkSelfPermission(this, permissions[0])) {
            requestPermissions(permissions, STORAGE_REQUEST)
        } else {
            if (currentMediaList.size == 0) {
                showDialog()
            }

            mediaViewModel.getMedia(null)
        }


        mediaViewModel.lvMediaData.observe(this, Observer {
            dismissDialog()
            if (cameraManager?.cameraPathUri != null) {
                return@Observer
            }
            MediaPreviewUtil.currentMediaList.clear()
            MediaPreviewUtil.currentMediaList.addAll(it)
            refreshMedia()
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
        rcyView.adapter = mediaItemAdapter
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
            if (!::mediaDirList.isInitialized) {
                return@setOnClickListener
            }
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
            cbOriginal?.isChecked?.let { it1 -> MediaGalleryActivity.actionStart(this, 0, it1, true,onMediaResult = mOnMediaResult) }
        }
    }

    private fun refreshMedia() {
        mediaItemAdapter.selectMediaList = SelectedMediaManager.selectMediaList
        updateTitle()
        mediaItemAdapter.notifyDataSetChanged()
        updateMediaPreview()
    }


    /**
     * camera click
     */


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCameraClick(view: View?) {
        if (!PermissionUtils.checkSelfPermission(this, permissionCamera[0])) {
            requestPermissions(permissionCamera, CAMERA_REQUEST)
        } else {
            openCamera()
        }

    }

    /**
     * 打开相机
     */
    private fun openCamera() {
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
            cbOriginal?.isChecked?.let {
                MediaGalleryActivity.actionStart(this, realPosition,
                    it, false, mOnMediaResult)
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    override fun onItemCheckClick(view: View?, position: Int, isChecked: Boolean) {

        var mediaItem = mediaItemAdapter.getMedia(position)
        if (SelectedMediaManager.selectMediaList.contains(mediaItem)) {
            SelectedMediaManager.selectMediaList.remove(mediaItem)
            mediaItemAdapter.notifyDataSetChanged()
        } else {
            if (SelectedMediaManager.selectMediaList.size > VanGoghConst.MAX_MEDIA - 1) {
                toast(getString(R.string.picture_message_max_num, VanGoghConst.MAX_MEDIA))
                mediaItemAdapter.notifyItemChanged(position)
                return
            }
            if (cbOriginal?.isChecked == true) {
                mediaItem.isOriginal = true
            }
            SelectedMediaManager.selectMediaList.add(mediaItem)
            mediaItemAdapter.notifyItemChanged(position)
        }
        mediaItemAdapter.selectMediaList = SelectedMediaManager.selectMediaList
        updateTitle()
        updateMediaPreview()
    }

    @SuppressLint("StringFormatMatches")
    private fun updateMediaPreview() {
        if (SelectedMediaManager.selectMediaList.size > 0) {
            mediaPreview.isEnabled = true
            mediaPreview.text = getString(R.string.media_preview_num, SelectedMediaManager.selectMediaList.size)
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
            MediaGalleryActivity.REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_CANCELED) {
                    if (data != null) {
                        cbOriginal?.isChecked =
                            data.getBooleanExtra(MediaGalleryActivity.IMAGE_ORIGINAL, false)
                        refreshMedia()
                    }
                }
            }

            CameraManager.REQUEST_CAMERA -> {
                if ((resultCode == Activity.RESULT_OK)) {

                    val cameraItem = MediaItem()
                    cameraItem.originalPath = cameraManager?.cameraRealPath
                    val actualImage = File(cameraItem.originalPath)
                    actualImage?.let { imageFile ->
                        lifecycleScope.launch {
                            val compressFile =
                                Compressor.compress(activity!!, imageFile) {
                                    default()
                                    size(VanGoghConst.COMPRESS_SIZE)//100K
                                    destination(imageFile)
                                }
                            cameraItem.compressPath = compressFile.absolutePath
                            cameraItem.path = cameraItem.compressPath
                            val widthAndHeight = ImageUtils.getImageSize(cameraItem.path)
                            val size = File(cameraManager?.cameraRealPath).length()
                            cameraItem.pathUri = cameraManager?.cameraPathUri
                            cameraItem.size = size
                            cameraItem.mineType = "image/jpeg"
                            cameraItem.dataToken = System.currentTimeMillis()
                            activity?.sendBroadcast(
                                Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.fromFile(File(cameraManager?.cameraRealPath))
                                )
                            )
                            cameraItem.width = widthAndHeight[0]
                            cameraItem.height = widthAndHeight[1]

                            if (SelectedMediaManager.selectMediaList.size >= VanGoghConst.MAX_MEDIA) {
                                toast(
                                    getString(
                                        R.string.picture_message_max_num,
                                        VanGoghConst.MAX_MEDIA
                                    )
                                )
                                return@launch
                            }
                            MediaPreviewUtil.currentMediaList.add(0, cameraItem)
                            SelectedMediaManager.selectMediaList.add(cameraItem)
                            refreshMedia()

                        }
                    }


                } else if (resultCode == Activity.RESULT_CANCELED) {
                    if (isCamera) {
                        finish()
                    }
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
        when (requestCode) {
            STORAGE_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showDialog()
                    mediaViewModel.getMedia(null)
                }
            }
            CAMERA_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }

            }
        }

    }


}