package com.vangogh.media.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.vangogh.media.utils.MediaTimeUtils.getCameraTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    private fun createCameraFile(): Uri? {
        val cameraFileName = "IMG_" + getCameraTime() + ".jpg"
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, cameraFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
          /*  put(MediaStore.MediaColumns.SIZE, 300*1024)
            put(MediaStore.MediaColumns.DATE_TAKEN, System.currentTimeMillis())*/
        }

        cameraRealPath = cameraFileName
        cameraPathUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return cameraPathUri
    }


    @WorkerThread
    @Throws(IOException::class)
    fun  cameraIntent(): Intent? {
        var cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

           // Log.e("CameraManager","currentThread = ${Thread.currentThread().name}")
            if (cameraIntent.resolveActivity(context.packageManager) != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val photoURI = createCameraFile()
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                } else {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, createCameraFile())
                }
            } else {
               return  null
            }


        return cameraIntent
    }


    fun deleteContentUri(path: Uri?) {
        if (path != null) {
            context.contentResolver.delete(path, null, null)
        }
    }

    companion object {

       const val REQUEST_CAMERA = 0x1024

    }

}