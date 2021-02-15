package com.vangogh.media.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.LinearLayout

/**
 * @author dhl
 * performClick checkBox
 */
class CheckSelectFrameLayout @JvmOverloads constructor (context : Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :LinearLayout(context, attrs, defStyleAttr){

    override fun performClick(): Boolean {
        return if (childCount >=1) {
            getChildAt(0).performClick()
        } else super.performClick()
    }

    override fun callOnClick(): Boolean {
        return if (childCount >= 1) {
            getChildAt(0).performClick()
        } else super.performClick()
    }
}

