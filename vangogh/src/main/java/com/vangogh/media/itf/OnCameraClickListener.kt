
package com.vangogh.media.itf

import android.view.View

/**
 * @author dhl camrea
 * Listens on the  camera click.
 */
interface OnCameraClickListener {
    /**
     * When Item is clicked.
     *
     * @param view     item view.
     * @param position item position.
     */
    fun onCameraClick(
        view: View?
    )
}