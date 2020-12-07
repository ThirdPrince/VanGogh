package com.vangogh.media.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author dhl
 * MediaDirectory
 */
@Parcelize
class MediaDirectory(
                var id: Long = 0,
                var bucketId: String? = null,
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



    override fun equals(other: Any?): Boolean {
        return this.bucketId == (other as? MediaDirectory)?.bucketId
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (bucketId?.hashCode() ?: 0)
        result = 31 * result + (coverPath?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + dateAdded.hashCode()
        result = 31 * result + medias.hashCode()
        return result
    }
}