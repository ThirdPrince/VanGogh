package com.vangogh.media.cache

import android.app.Application
import com.core.log.EasyLog
import java.io.File
import java.security.AccessControlContext

/**
 * @Title: $
 * @Package $
 * @Description: $(用一句话描述)
 * @author $
 * @date $
 * @version V1.0
 */

const val TAG = "LRUImageCache"

class LRUImageCache<T, U>(private val capacity:Int) : LinkedHashMap<String, Long>(16,0.75f,true) {




    private  var maxSize = 0

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Long>?): Boolean {

//        this.entries.forEach {
//            EasyLog.e(TAG,"it = ${it.value}")
//            maxSize += it.value
//        }
        maxSize = this.entries.size
        val imageCacheKey = eldest?.key
        val beyondMaxSize = maxSize > capacity
        if(beyondMaxSize){
          File(imageCacheKey).delete()
        }
        return beyondMaxSize
    }

}