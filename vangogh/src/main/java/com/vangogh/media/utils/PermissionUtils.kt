package com.vangogh.media.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * @Title: PermissionUtils
 * @Package com.vangogh.media.utils
 * @Description: 权限申请Utils
 * @author dhl
 * @date 2021 10 29
 * @version V1.0
 */
object PermissionUtils {

    /**
     * check Permission
     */
    fun checkSelfPermission(ctx: Context, permission: String?): Boolean {
        return (ContextCompat.checkSelfPermission(ctx.applicationContext, permission!!)
                == PackageManager.PERMISSION_GRANTED)
    }

}