package com.vangogh.media.utils

import androidx.appcompat.app.AppCompatActivity
import com.media.vangogh.R
import com.vangogh.media.ui.fragment.BaseFragment

/**
 * Fragment add  replace
 */
object FragmentUtils {

    fun replaceFragment(activity: AppCompatActivity, contentId: Int, fragment: BaseFragment) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out)
        transaction.replace(contentId, fragment, fragment.javaClass.simpleName)
       // transaction.addToBackStack(null)
        transaction.commit()
    }
}