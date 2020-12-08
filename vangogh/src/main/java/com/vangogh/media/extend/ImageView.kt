package com.vangogh.media.extend

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.media.vangogh.R
import java.io.File
fun ImageView.loadUrl(url:String,requestOptions: RequestOptions){
    Glide.with(this).asBitmap().load(url).transition(withCrossFade()).apply(requestOptions).into(this)
}