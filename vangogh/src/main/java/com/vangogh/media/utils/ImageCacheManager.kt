package com.vangogh.media.utils

import com.vangogh.media.cache.LRUImageCache
import com.vangogh.media.config.VanGoghConst


/**
 * @Title: ImageCacheManager
 * @Package $
 * @Description: compressImage cache
 * @author dhl
 * @date 2021 12
 * @version V1.0
 */
object ImageCacheManager {

    const val MAX_IMG_CACHE_SIZE = 2.shl(4)

    var lruImageCache = LRUImageCache<String,Long>(MAX_IMG_CACHE_SIZE)

}