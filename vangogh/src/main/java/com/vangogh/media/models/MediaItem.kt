package com.vangogh.media.models

import android.icu.text.AlphabeticIndex
import android.net.Uri
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize
import java.time.Duration

/**
 * @ClassName MediaItem
 * @Description  media contains image +video
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
private const val compressSize = 100* 1024  // 100Kb
@Parcelize
data class MediaItem(
    var id: Long = 0,
    var bucketId:Long = 0,
    var bucketName:String ?= null,
    var name: String? = null,
    var pathUri: Uri? = null,
    var path: String? = null,
    var compressPath:String?=null,
    var size: Long = 0,
    var width: Int = 0,
    var height: Int = 0,
    var mineType: String? = null,
    var duration: Long = 0,
    var dataToken: Long = 0
) :
    Parcelable {

    override fun toString(): String {
        return "path = $path \n" +
                "compressPath = $compressPath \n"+
                "size = $size \n"+
                "width = $width \n" +
                "height = $height"
    }

    fun isGif():Boolean{
        return mineType!!.endsWith("gif")
    }

    fun isVideo():Boolean{
        return mineType!!.startsWith("video")
    }

    /**
     * not gif video
     */
    fun isImage():Boolean{
        if(mineType!!.startsWith("video"))
            return false
        return !mineType!!.endsWith("gif")
    }

    /**
     * isCompress just isImage and compress  size > 100K
     *
     */
    fun isCompress():Boolean{
        return isImage() && size > compressSize
    }
}