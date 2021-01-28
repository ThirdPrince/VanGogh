package com.vangogh.media.utils

import android.provider.MediaStore

/**
 * @ClassName MediaSelectArgs
 * @Description MediaSelectArgs
 * @Author dhl query condition
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
            + " AND " + MediaStore.MediaColumns.SIZE + ">0")

    /**
     * image + video(like wechat)   use with MEDIA_SELECTION
     */
    val MEDIA_SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )


    /**
     * (image  + video)
     */
    const val MEDIA_SELECTION_NOT_GIF = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")

    val MEDIA_SELECTION_ARGS_NOT_GIF = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
        "image/gif"
    )

    /**
     * imageSelection for gif
     */
    const val IMAGE_SELECTION =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + MediaStore.MediaColumns.MIME_TYPE + "=?")

    /**
     * just image(image contains gif)
     */
    val IMAGE_SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
    )


    const val IMAGE_SELECTION_NOT_GIF =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + " AND " + MediaStore.MediaColumns.MIME_TYPE + "!=?")


    /**
     * just gif  use with IMAGE_SELECTION
     */
    val GIF_SELECTION_ARGS =
        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), "image/gif")


}