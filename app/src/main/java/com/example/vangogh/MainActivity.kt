package com.example.vangogh
import android.media.browse.MediaBrowser
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vangogh.adapter.GridMediaAdapter
import com.vangogh.media.divider.GridSpacingItemDecoration
import com.vangogh.media.models.MediaItem
import com.vangogh.media.ui.activity.SelectMediaActivity


class MainActivity : AppCompatActivity() {

    private lateinit var gridMediaAdapter: GridMediaAdapter
    private  var mediaList = mutableListOf<MediaItem>()
    private lateinit var rcy_view: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        SelectMediaActivity.actionStart(this)
        rcy_view = findViewById(R.id.rcy_view)
        val layoutManager = GridLayoutManager(this, 4)
        rcy_view.layoutManager = layoutManager
        rcy_view.itemAnimator = DefaultItemAnimator()
        rcy_view.addItemDecoration(GridSpacingItemDecoration(4, 5, false))
        gridMediaAdapter = GridMediaAdapter(this,mediaList)
        rcy_view.adapter = gridMediaAdapter
    }
}