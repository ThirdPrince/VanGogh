package com.vangogh.media.view

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * TODO: document your custom view class.
 */
class SquareImageView : AppCompatImageView {


    var  mConfig: Configuration = resources.configuration

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (mConfig!!.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                super.onMeasure(widthMeasureSpec, widthMeasureSpec)
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                super.onMeasure(heightMeasureSpec, heightMeasureSpec)
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }


}