package com.vangogh.media.models

import android.net.Uri
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import kotlinx.android.parcel.Parcelize

/**
 * @author dhl
 * image+video
 *
 */
@Parcelize
class MediaItem (var id: Long = 0,var name:String ?= null, var pathUri: Uri ?= null, var path:String ?= null, var size:Long =0, var width:Int = 0, var height:Int = 0, var mineType:String ?= null, var dataToken:Long = 0) :
    Parcelable {
}