package com.vangogh.media.extend

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.widget.Toast

/**
 * @ClassName Context
 * @Description toast
 * @Author dhl
 * @Date 2021/1/28 14:24
 * @Version 1.0
 */
fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

fun Context.getScreenHeight(): Int {

    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        ?: return -1
    val point = Point()
    wm.defaultDisplay.getSize(point)
    return point.y
}