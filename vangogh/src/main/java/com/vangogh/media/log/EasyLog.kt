package com.core.log

import android.os.Build
import android.util.Log
import com.media.vangogh.BuildConfig


/**
 * 规范日志
 * @author dhl
 *
 */
object EasyLog{

    @JvmStatic
    fun v(tag:String,msg:String){
        if(BuildConfig.DEBUG) {
            Log.v(tag, msg)
        }
    }
    @JvmStatic
    fun d(tag: String,msg:String){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    @JvmStatic
    fun i(tag: String,msg: String){
        if(BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }
    @JvmStatic
    fun e(tag: String,msg: String){
        if(BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

}