package com.vangogh.media.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import com.vangogh.media.utils.MediaTimeUtils.getCameraTime
import java.io.File
import java.io.IOException


/**
 * @ClassName CameraManager
 * @Description dhl
 * @Author aa
 * @Date 2021/2/23 9:42
 * @Version 1.0
 */
class CameraManager(val context: Context) {


    var cameraPathUri: Uri? = null
    var cameraRealPath: String? = null

    @Throws(IOException::class)
    private fun createCameraUri(): Uri? {
        val cameraFileName = "IMG_" + getCameraTime() + ".jpg"
        val cameraFile = File( Environment.getExternalStorageDirectory(),cameraFileName)
        cameraPathUri = if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(context, VanGoghProvider.getProvideName(context), cameraFile)
        } else {
            Uri.fromFile(cameraFile)
        }


        cameraRealPath = cameraFile.absolutePath

        return cameraPathUri
    }


    @WorkerThread
    @Throws(IOException::class)
    fun  cameraIntent(): Intent? {
        var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createCameraUri())

            } else {
               return  null
            }


        return cameraIntent
    }



    companion object {

       const val REQUEST_CAMERA = 0x1024

    }

}