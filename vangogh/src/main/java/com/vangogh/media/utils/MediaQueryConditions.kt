package com.vangogh.media.utils

import android.provider.MediaStore
import com.vangogh.media.config.VanGoghConst

/**
 * @ClassName MediaQueryConditions
 * @Description MediaSelectArgs  query condition
 * @Author dhl
 * @Date 2021/1/28 10:30
 * @Version 1.0
 */
object MediaQueryConditions {


    /**
     * mediaSelection (image + gif + video)
     */
    const val MEDIA_SELECTION = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + " AND " + MediaStore.MediaColumns.WIDTH + ">0"
            + " AND " + MediaStore.MediaColumns.SIZE + "<?")

    /**
     * image + video(like wechat)   use with MEDIA_SELECTION
     */
    val MEDIA_SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
        VanGoghConst.MEDIA_MAX_SIZE.toString()
    )


    /**
     * (image  + video)
     */
    const val MEDIA_SELECTION_NOT_GIF = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + " AND " + MediaStore.MediaColumns.WIDTH + ">0"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")

    /**
     * media without gif
     */
    val MEDIA_SELECTION_ARGS_NOT_GIF = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
        "image/gif"
    )

    /**
     * imageSelection (image contains gif)
     */
    const val IMAGE_SELECTION =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + MediaStore.MediaColumns.WIDTH + ">0"
                )

    /**
     * just image(image contains gif)
     */
    val IMAGE_SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
    )

    /**
     * image without gif
     */
    const val IMAGE_SELECTION_NOT_GIF =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + MediaStore.MediaColumns.WIDTH + ">0"
                + " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")

    /**
     * just gif  use with IMAGE_SELECTION
     */
    val GIF_SELECTION_ARGS =
        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), "image/gif")


    /**
     * only gif
     */
    const val GIF_SELECTION =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + MediaStore.MediaColumns.WIDTH + ">0"
                + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?")

    /**
     * only gif
     */
    const val VIDEO_SELECTION =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + MediaStore.MediaColumns.WIDTH + ">0"
                + " AND " + MediaStore.MediaColumns.SIZE + "<?")

    /**
     * just image(image contains gif)
     */
    val VIDEO_SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
        VanGoghConst.MEDIA_MAX_SIZE.toString()
    )
}