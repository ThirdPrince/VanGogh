package com.vangogh.media.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.media.vangogh.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * BaseFragment
 */
abstract  class BaseFragment : Fragment() {


    open val uiScope = CoroutineScope(Dispatchers.Main)



}