package com.vangogh.media.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatDialog
import com.media.vangogh.R


/**
 * @ClassName LoadingDialog
 * @Description  LoadingDialog
 * @Author dhl
 * @Date 2021/2/20 9:36
 * @Version 1.0.1
 */
class LoadingDialog : AppCompatDialog {

    constructor(context: Context) : this(context,0)
    constructor(context: Context, themeResId: Int) : super(context, R.style.Picture_Theme_AlertDialog){

        setCancelable(true)
        setCanceledOnTouchOutside(true)
         window?.setWindowAnimations(R.style.PictureThemeDialogWindowStyle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog_layout)
    }
}