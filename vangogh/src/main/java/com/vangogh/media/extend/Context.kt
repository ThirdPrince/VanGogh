package com.vangogh.media.extend

import android.content.Context
import android.widget.Toast

/**
 * @ClassName Context
 * @Description toast
 * @Author dhl
 * @Date 2021/1/28 14:24
 * @Version 1.0
 */
fun Context.toast(message:CharSequence)= Toast.makeText(this,message, Toast.LENGTH_LONG).show()