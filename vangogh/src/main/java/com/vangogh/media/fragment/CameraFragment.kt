package com.vangogh.media.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.core.log.EasyLog
import com.media.vangogh.R
import com.vangogh.media.config.VanGogh
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.config.VanGoghConst.COMPRESS_SIZE
import com.vangogh.media.extend.toast
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.CAMERA_REQUEST
import com.vangogh.media.ui.activity.GalleryActivity
import com.vangogh.media.ui.activity.STORAGE_REQUEST
import com.vangogh.media.utils.CameraManager
import com.vangogh.media.utils.ImageUtils
import com.vangogh.media.utils.MediaPreviewUtil
import com.vangogh.media.utils.PermissionUtils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * for camera
 */
class CameraFragment : Fragment() {


    companion object {
        const val TAG = "CameraFragment"
    }

    private val permissionCamera = arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)

    val uiScope = CoroutineScope(Dispatchers.Main)

    private var cameraManager: CameraManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!PermissionUtils.checkSelfPermission(context!!, permissionCamera[0])) {
            requestPermissions(permissionCamera, CAMERA_REQUEST)
        } else {
            openCamera()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    /**
     * 打开相机
     */
    private fun openCamera() {
        cameraManager = CameraManager(context!!)
        try {
            uiScope.launch {
                val cameraIntent = withContext(Dispatchers.IO) {
                    cameraManager?.cameraIntent()
                }
                if (cameraIntent != null)
                    startActivityForResult(cameraIntent, CameraManager.REQUEST_CAMERA)
                else
                    Toast.makeText(context, getString(R.string.no_camera_exists), Toast.LENGTH_LONG)
                        .show()

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                }

            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CameraManager.REQUEST_CAMERA -> {
                if ((resultCode == Activity.RESULT_OK)) {
                    val cameraItem = MediaItem()
                    cameraItem.originalPath = cameraManager?.cameraRealPath
                    val actualImage = File(cameraItem.originalPath)
                    actualImage?.let { imageFile ->
                        lifecycleScope.launch {
                            val compressFile =
                                Compressor.compress(activity!!, imageFile){
                                    default()
                                    size(COMPRESS_SIZE)//100K
                                    destination(imageFile)
                                }
                            cameraItem.compressPath = compressFile.absolutePath
                            cameraItem.path = cameraItem.compressPath
                            val widthAndHeight = ImageUtils.getImageSize(cameraItem.path)
                            val size = File(cameraManager?.cameraRealPath).length()
                            cameraItem.pathUri = cameraManager?.cameraPathUri
                            cameraItem.size = size
                            cameraItem.mineType = "image/jpeg"
                            cameraItem.dataToken = System.currentTimeMillis()
                            activity?.sendBroadcast(
                                Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.fromFile(File(cameraManager?.cameraRealPath))
                                )
                            )
                            cameraItem.width = widthAndHeight[0]
                            cameraItem.height = widthAndHeight[1]
                            EasyLog.e(TAG,"compressFile path = ${compressFile.absolutePath}")
                            EasyLog.e(TAG,"compressFile size = ${compressFile.length()/1024}")

                            VanGogh.mOnCameraResult.onResult(cameraItem)
                            activity?.supportFragmentManager?.beginTransaction()
                                ?.remove(this@CameraFragment)?.commitAllowingStateLoss()
                            return@launch
                        }

                    }

                } else if (resultCode == Activity.RESULT_CANCELED) {
                    activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
                }

            }


        }
    }

}