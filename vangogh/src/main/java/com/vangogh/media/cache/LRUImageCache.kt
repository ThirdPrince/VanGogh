package com.vangogh.media.cache

import android.app.Application
import com.core.log.EasyLog
import com.vangogh.media.config.VanGogh
import com.vangogh.media.models.MediaItem
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

    private  lateinit var deleFile :File

    override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, Long>?): Boolean {

        maxSize = size
        val imageCacheKey = eldest?.key
        val beyondMaxSize = maxSize >= capacity
        if(beyondMaxSize){
            deleFile = File(imageCacheKey)

            if(deleFile?.exists()){
                deleFile?.delete()
            }
        }
        return beyondMaxSize
    }

}