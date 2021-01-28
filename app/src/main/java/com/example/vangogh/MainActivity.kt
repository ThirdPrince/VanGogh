package com.example.vangogh
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vangogh.adapter.GridMediaAdapter
import com.example.vangogh.itf.OnAddMediaListener
import com.vangogh.media.config.VanGogh
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.SelectMediaActivity
import com.vangogh.media.itf.OnMediaResult as OnMediaResult


class MainActivity : AppCompatActivity(),OnAddMediaListener{

    private lateinit var gridMediaAdapter: GridMediaAdapter
    private  var mediaList = mutableListOf<MediaItem>()
    private lateinit var rcy_view: RecyclerView
    private lateinit var activity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activity = this
        rcy_view = findViewById(R.id.rcy_view)
        val layoutManager = GridLayoutManager(this, 4)
        rcy_view.layoutManager = layoutManager
        rcy_view.itemAnimator = DefaultItemAnimator()
        rcy_view.addItemDecoration(GridSpacingItemDecoration(4, 5, false))
        gridMediaAdapter = GridMediaAdapter(this,mediaList)
        gridMediaAdapter.onAddMediaListener = activity
        rcy_view.adapter = gridMediaAdapter


    }

    override fun onAddMediaClick() {
        VanGogh.getMedia().startForResult(this).onMediaResult = object:OnMediaResult{
            override fun onResult(mediaList: List<MediaItem>) {
                gridMediaAdapter = GridMediaAdapter(activity,mediaList)
                rcy_view.adapter = gridMediaAdapter
                gridMediaAdapter.onAddMediaListener = activity
            }

        }

    }
}