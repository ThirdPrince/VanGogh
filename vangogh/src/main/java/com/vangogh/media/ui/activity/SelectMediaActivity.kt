package com.vangogh.media.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.media.vangogh.R
import com.vangogh.media.ui.fragment.BaseFragment
import com.vangogh.media.ui.fragment.SelectMediaFragment
import com.vangogh.media.utils.FragmentUtils

/**
 * @author dhl
 * MediaPicker UI
 * contains  image video
 *
 */
class SelectMediaActivity : AppCompatActivity() {


    companion object{
        fun actionStart(activity: AppCompatActivity){
            val intent  = Intent(activity,
                SelectMediaActivity::class.java).apply {
                putExtra("selectMedia","")
            }
            activity.startActivity(intent)

        }
    }

    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
    lateinit var selectMediaFragment: SelectMediaFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_media)
        val checkPermission = this?.let{ ActivityCompat.checkSelfPermission(it,permissions[0])}
        if(checkPermission != PackageManager.PERMISSION_GRANTED){
            //这是系统的方法
            requestPermissions(permissions,0)
        }else{
            initSelectFragment()
        }
       // selectMediaFragment = SelectMediaFragment.newInstance("","")
        //FragmentUtils.replaceFragment(this,android.R.id.content,selectMediaFragment)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            initSelectFragment()
        }
    }
    private fun initSelectFragment(){
        selectMediaFragment = SelectMediaFragment.newInstance("","")
        FragmentUtils.replaceFragment(this,R.id.content_fragment,selectMediaFragment)
    }
}