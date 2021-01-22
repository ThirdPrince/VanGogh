package com.vangogh.media.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewStub
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaGridItemAdapter
import com.vangogh.media.config.VanGogh
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.itf.OnItemCheckListener
import com.vangogh.media.itf.OnItemClickListener
import com.vangogh.media.itf.OnMediaResult
import com.vangogh.media.models.MediaItem
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.viewmodel.MediaViewModel
import com.vangogh.media.viewmodel.SelectMediaViewModel
import kotlinx.android.synthetic.main.activity_select_media.*
import kotlinx.android.synthetic.main.activity_select_media.view.*
import kotlinx.android.synthetic.main.media_grid_top_bar.*
import kotlinx.android.synthetic.main.media_select_button.*
import kotlinx.coroutines.launch

/**
 * @ClassName SelectMediaActivity
 * @Description  MediaUI
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
class SelectMediaActivity : BaseSelectActivity(), View.OnClickListener, OnItemClickListener,
    OnItemCheckListener {

    companion object {
        fun actionStart(activity: FragmentActivity) {
            val intent = Intent(
                activity,
                SelectMediaActivity::class.java
            ).apply {
                putExtra("selectMedia", "")
            }
            activity.startActivity(intent)

        }
    }


    private lateinit var mediaViewModel: MediaViewModel

    private lateinit var mediaItemAdapter: MediaGridItemAdapter


    /**
     * permissions
     */
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_media)
        initSendMediaListener()
        mediaViewModel =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                MediaViewModel::class.java
            )

        val checkPermission = this?.let { ActivityCompat.checkSelfPermission(it, permissions[0]) }
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            //这是系统的方法
            requestPermissions(permissions, 0)
        } else {
            mediaViewModel.getMedia(null)
        }
        mediaViewModel.lvMediaData.observe(this, Observer {
            mediaItemAdapter = MediaGridItemAdapter(this, it)
            val layoutManager = GridLayoutManager(this, 4)
            rcy_view.layoutManager = layoutManager
            rcy_view.itemAnimator = DefaultItemAnimator()
            rcy_view.addItemDecoration(GridSpacingItemDecoration(4, 5, false))
            rcy_view.adapter = mediaItemAdapter
            mediaItemAdapter!!.onItemClickListener = this
            mediaItemAdapter!!.onItemCheckListener = this
            MediaPreviewUtil.mediaItemList = it

        })


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mediaViewModel.getMedia(null)
        }
    }


    override fun onItemClick(view: View?, position: Int) {
        GalleryActivity.actionStart(this, position)
    }

    override fun onItemCheckClick(view: View?, position: Int, isChecked: Boolean) {
        var mediaItem = mediaItemAdapter.items[position]
        if (isChecked) {
            selectMediaList.add(mediaItem)
        } else {
            selectMediaList.remove(mediaItem)

        }
        mediaItemAdapter.selectMediaList = selectMediaList
        if (selectMediaList.size > 0) {
            media_send.isEnabled = true
            media_send.text = getString(R.string.media_send_num, selectMediaList.size, 9)
        } else {
            media_send.isEnabled = false
            media_send.text = resources.getString(R.string.media_send_not_enable)
        }
    }


}