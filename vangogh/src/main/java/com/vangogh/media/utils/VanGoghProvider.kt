package com.vangogh.media.utils

import android.content.Context
import androidx.core.content.FileProvider

/**
 * @ClassName VanGoghProvider
 * @Description fileProvider
 * @Author dhl
 * @Date 2021/2/27 9:42
 * @Version 1.0
 */
class VanGoghProvider : FileProvider(){

    companion object {
        fun getProvideName(context: Context): String {
            return context.packageName + ".vangogh.provider"
        }
    }
}