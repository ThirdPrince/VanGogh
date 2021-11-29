package com.vangogh.media.life

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.vangogh.media.config.VanGoghConst

/**
 * @Title: VanGoghLifeObserver
 * @Package $
 * @Description: Vangogh life
 * @author dhl
 * @date 2021
 * @version V1.0.4
 */
class VanGoghLifeObserver :LifecycleObserver{

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        VanGoghConst.reset()
    }
}