package com.vangogh.media.config

import androidx.annotation.IntDef
import com.vangogh.media.itf.OnAvatarResult
import com.vangogh.media.utils.SelectedMediaManager


/**
 * @ClassName VanGoghConst
 * @Description some default value
 * @Author dhl
 * @Date 2021/1/26 10:25
 * @Version 1.0
 */
object VanGoghConst {

    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    @MustBeDocumented
    @IntDef(
        MediaType.MediaAll,
        MediaType.MediaOnlyImage,
        MediaType.MediaOnlyVideo,
        MediaType.MediaOnlyGif
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation
    class MediaType {
        companion object {
            const val MediaAll = 0
            const val MediaOnlyImage = 1
            const val MediaOnlyVideo = 2
            const val MediaOnlyGif = 3
        }
    }


    @Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
    @MustBeDocumented
    @IntDef(MediaTitle.MediaComplete, MediaTitle.MediaSend)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation
    class MediaTitle {
        companion object {
            const val MediaComplete = 0
            const val MediaSend = 1

        }
    }

    var MAX_MEDIA = 9

    var GRID_SPAN_CONT = 4

    var MEDIA_MAX_SIZE = Int.MAX_VALUE

    var VIDEO_MAX_DURATION = Int.MAX_VALUE

    var MEDIA_TYPE = MediaType.MediaAll

    var MEDIA_TITLE = MediaTitle.MediaComplete

    var CAMERA_ENABLE: Boolean = false

    var COMPRESS_SIZE = 100 * 1024L // 100Kb




    fun reset() {
        MEDIA_TITLE = MediaTitle.MediaComplete
        MEDIA_TYPE = MediaType.MediaAll
        MAX_MEDIA = 9
        CAMERA_ENABLE = false
        SelectedMediaManager.selectMediaList.clear()
    }


}
