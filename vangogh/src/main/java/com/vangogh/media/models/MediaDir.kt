package com.vangogh.media.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @ClassName MediaDir
 * @Description  media Dir
 * @Author dhl
 * @Date 2020/12/22 9:36
 * @Version 1.0
 */
@Parcelize
data class MediaDir(
                var id: Long = 0,
                var bucketId: Long = 0,
                private var coverPath: Uri? = null,
                var name: String? = null,
                var dateAdded: Long = 0,
                val medias: MutableList<MediaItem> = mutableListOf()
) : Parcelable {

    fun getCoverPath(): Uri? {
        return when {
            medias.size > 0 -> medias[0].pathUri
            coverPath != null -> coverPath
            else -> null
        };
    }

    fun setCoverPath(coverPath: Uri?) {
        this.coverPath = coverPath
    }

    override fun toString(): String {
        return name.toString()
    }

    override fun equals(other: Any?): Boolean {
        return this.bucketId == (other as? MediaDir)?.bucketId
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (bucketId?.hashCode() ?: 0)
        return result
    }


}