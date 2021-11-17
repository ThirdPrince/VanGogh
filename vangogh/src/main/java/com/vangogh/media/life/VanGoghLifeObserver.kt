package com.vangogh.media.life

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.vangogh.media.config.VanGoghConst

/**
 * @Title: $
 * @Package $
 * @Description: Vangogh life
 * @author $
 * @date $
 * @version V1.0
 */
class VanGoghLifeObserver :LifecycleObserver{

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        VanGoghConst.reset()
    }
}