package com.vangogh.media.viewholder

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.vangogh.media.itf.OnCameraClickListener

/**
 * @ClassName CameraViewHolder
 * @Description for camera
 * @Author dhl
 * @Date 2021/2/22 10:25
 * @Version 1.0
 */
class CameraViewHolder(
    var activity: Activity,
    val view: View,
    var onCameraClickListener: OnCameraClickListener
) : RecyclerView.ViewHolder(view), View.OnClickListener {

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            view -> onCameraClickListener.onCameraClick(v)
        }

    }

}