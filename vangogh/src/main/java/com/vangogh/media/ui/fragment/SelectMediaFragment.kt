package com.vangogh.media.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaGridItemAdapter
import com.vangogh.media.config.VanGoghConst
import com.vangogh.media.viewmodel.MediaViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SelectMediaFragment.newInstance] factory method to
 * create an instance of this fragment.
 * SelectMedia fragment
 */
class SelectMediaFragment : BaseFragment() {

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SelectMediaFragment.
         */

        const val TAG = "SelectMediaFragment"
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SelectMediaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mediaViewModel:MediaViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var mediaItemAdapter: MediaGridItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rcy_view)
        mediaViewModel  = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MediaViewModel::class.java)
        mediaViewModel.getMedia(null, VanGoghConst.MEDIA_TYPE_IMAGE)
       /* mediaViewModel.lvMediaData.observe(this, Observer {
            mediaItemAdapter = MediaItemAdapter(this,it)
            val layoutManager = GridLayoutManager(activity, 4)
            recyclerView.layoutManager = layoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.addItemDecoration(GridSpacingItemDecoration(4,5,false))
            recyclerView.adapter = mediaItemAdapter


        })*/

    }


}