package com.vangogh.media.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaGridItemAdapter
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.itf.OnItemClickListener
import com.vangogh.media.ui.fragment.SelectMediaFragment
import com.vangogh.media.viewmodel.MediaViewModel
import kotlinx.android.synthetic.main.activity_select_media.*
import kotlinx.android.synthetic.main.media_grid_top_bar.*

/**
 * @author dhl
 * MediaPicker UI
 * contains  image video show media in grid
 */
class SelectMediaActivity : AppCompatActivity(),View.OnClickListener,OnItemClickListener {

   //  const val TAG = "SelectMediaActivity"

    companion object{
        fun actionStart(activity: AppCompatActivity){
            val intent  = Intent(activity,
                SelectMediaActivity::class.java).apply {
                putExtra("selectMedia","")
            }
            activity.startActivity(intent)

        }
    }


    private lateinit var mediaViewModel: MediaViewModel
    private lateinit var mediaItemAdapter: MediaGridItemAdapter

    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    lateinit var selectMediaFragment: SelectMediaFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_media)
        mediaViewModel  = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(MediaViewModel::class.java)
        val checkPermission = this?.let{ ActivityCompat.checkSelfPermission(it,permissions[0])}
        if(checkPermission != PackageManager.PERMISSION_GRANTED){
            //这是系统的方法
            requestPermissions(permissions,0)
        }else{
            mediaViewModel.getMedia(null)
        }
        initListener()
        mediaViewModel.lvMediaData.observe(this, Observer {
            mediaItemAdapter = MediaGridItemAdapter(this,it)
            val layoutManager = GridLayoutManager(this, 4)
            rcy_view.layoutManager = layoutManager
            rcy_view.itemAnimator = DefaultItemAnimator()
            rcy_view.addItemDecoration(GridSpacingItemDecoration(4,5,false))
            rcy_view.adapter = mediaItemAdapter
            mediaItemAdapter!!.onItemClickListener = this

        })

    }

   private  fun initListener(){
        mediaLeftBack.setOnClickListener(this)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            mediaViewModel.getMedia(null)
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.mediaLeftBack -> finish()
       }
    }

    override fun onItemClick(view: View?, position: Int) {
       Toast.makeText(this,"pos = ${position}",Toast.LENGTH_LONG).show()
    }

}