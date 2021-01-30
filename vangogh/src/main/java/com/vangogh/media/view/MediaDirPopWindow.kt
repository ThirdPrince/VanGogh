package com.vangogh.media.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R

/**
 * @ClassName MediaDirPopWindow
 * @Description MediaDirPopWindow
 * @Author aa
 * @Date 2021/1/30 14:44
 * @Version 1.0
 */
class MediaDirPopWindow(context: Context) :PopupWindow() {

    private val window by lazy {
        LayoutInflater.from(context).inflate(R.layout.media_dir_pop, null)
    }
    private val mRecyclerView by lazy {
        window.findViewById<RecyclerView>(R.id.dir_list)
    }

    private val isDismiss = false
    private val ivArrowView: ImageView? = null
    private val maxHeight = 0
    private val rootViewBg: View? = null

     init {
         contentView = window
     }

}


