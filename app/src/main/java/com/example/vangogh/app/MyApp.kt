package com.example.vangogh.app

import android.app.Application
import com.blankj.utilcode.util.CrashUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils

/**
 * @ClassName MyApp
 * @Description TODO
 * @Author aa
 * @Date 2021/1/21 11:15
 * @Version 1.0
 */
class MyApp :Application() {

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        CrashUtils.init()
        
    }
}