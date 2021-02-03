package com.vangogh.media.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.media.vangogh.R
import com.vangogh.media.adapter.MediaDirAdapter
import com.vangogh.media.extend.getScreenHeight
import com.vangogh.media.itf.OnMediaItemClickListener
import com.vangogh.media.models.MediaDir

/**
 * @ClassName MediaDirPopWindow
 * @Description MediaDirPopWindow
 * @Author aa
 * @Date 2021/1/30 14:44
 * @Version 1.0
 */
class MediaDirPopWindow(val context: Context, var items: List<MediaDir>, var ivArrow: ImageView) :
    PopupWindow(), View.OnClickListener {

    private val window by lazy {
        LayoutInflater.from(context).inflate(R.layout.media_dir_pop, null)
    }

    private val rootView by lazy {
        window.findViewById<RelativeLayout>(R.id.rootView)
    }
    private val recyclerView by lazy {
        window.findViewById<RecyclerView>(R.id.rcy_view)
    }

    lateinit var mediaDirAdapter: MediaDirAdapter

    var dirCheckPosition
        set(value) {
            mediaDirAdapter.dirCheckPosition = value
            mediaDirAdapter.notifyDataSetChanged()
        }

    get() =  mediaDirAdapter.dirCheckPosition

    private val isDismiss = false
    private val ivArrowView: ImageView? = null
    private var maxHeight = 0


    init {
        contentView = window
        width = RelativeLayout.LayoutParams.MATCH_PARENT
        height = RelativeLayout.LayoutParams.WRAP_CONTENT
        animationStyle = R.style.PictureThemeWindowStyle
        isFocusable = true
        isOutsideTouchable = true
        rootView.setOnClickListener(this)
        maxHeight = (context.getScreenHeight() * 0.65).toInt()
        val listParams: ViewGroup.LayoutParams = recyclerView.layoutParams
        recyclerView.layoutParams
        listParams.height = if (items.size > 8) maxHeight else ViewGroup.LayoutParams.WRAP_CONTENT
        recyclerView.layoutParams = listParams
        this.update()
        initView()
    }

    private fun initView() {
        mediaDirAdapter = MediaDirAdapter(context, items)
        mediaDirAdapter.onMediaItemClickListener
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mediaDirAdapter
    }


    fun setOnMediaItemClickListener(listener: OnMediaItemClickListener?) {
        mediaDirAdapter.onMediaItemClickListener = listener
    }

    override fun showAsDropDown(anchor: View?) {
        super.showAsDropDown(anchor)
        setArrowRotation()
    }

    override fun dismiss() {
        super.dismiss()
        setArrowRotation()
    }


    override fun onClick(v: View?) {
        dismiss()
    }

    private fun setArrowRotation() {
        ivArrow.animate().setDuration(200).rotationBy(180f)
    }

}


