package com.vangogh.media.utils

import com.vangogh.media.cache.LRUImageCache
import com.vangogh.media.config.VanGoghConst


/**
 * @Title: $
 * @Package $
 * @Description: compressImage cache
 * @author $
 * @date $
 * @version V1.0
 */
object ImageCacheManager {

    const val MAX_IMG_CACHE_SIZE = 2.shl(3)

    var lruImageCache = LRUImageCache<String,Long>(MAX_IMG_CACHE_SIZE)

}