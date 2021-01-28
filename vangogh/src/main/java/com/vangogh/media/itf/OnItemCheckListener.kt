
package com.vangogh.media.itf

import android.view.View

/**
 * @author dhl
 * Listens on the item's  checkclick.
 */
interface OnItemCheckListener {
    /**
     * When Item is clicked.
     *
     * @param view     item view.
     * @param position item position.
     */
    fun onItemCheckClick(
        view: View?,
        position: Int,
        isChecked: Boolean
    )
}