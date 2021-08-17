package com.tntra.pargo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.adapter.LiveStreamListAdapter
import com.tntra.pargo.common.onClickAdapter

class GoLiveScreenActivity : AppCompatActivity(), onClickAdapter {

    private var memberRecycView: RecyclerView? = null
    private var liveStreamListAdapter: LiveStreamListAdapter? = null
    private var liveStreamList: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_go_live_screen)

        initView()
    }

    private fun initView() {
        memberRecycView = findViewById(R.id.memberRecycView)
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        for (i in 1..5) {
            if (i == 1) {
                liveStreamList?.add("https://picsum.photos/id/117/1544/1024")
            } else if (i == 2) {
                liveStreamList?.add("https://picsum.photos/id/131/4698/3166")
            } else if (i == 3) {
                liveStreamList?.add("https://picsum.photos/id/14/2500/1667")
            } else if (i == 4) {
                liveStreamList?.add("https://picsum.photos/id/156/2177/3264")
            } else {
                liveStreamList?.add("https://picsum.photos/id/117/1544/1667")
            }
        }
        liveStreamListAdapter = LiveStreamListAdapter(liveStreamList!!, this, this)
        memberRecycView?.layoutManager = GridLayoutManager(this, 2)
        memberRecycView?.adapter = liveStreamListAdapter

        val params = RelativeLayout.LayoutParams(
                width,
                height
        )
        memberRecycView?.layoutParams = params
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

}