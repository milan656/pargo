package com.tntra.pargo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tntra.pargo.R
import com.tntra.pargo.adapter.AboutAdapter
import com.tntra.pargo.common.onClickAdapter

class EventActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var ivBack: ImageView? = null
    private var tvTitle: TextView? = null
    private var aboutRecycView: RecyclerView? = null
    private var aboutAdapter: AboutAdapter? = null
    private var aboutList: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        initView()
    }

    private fun initView() {
        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        aboutRecycView = findViewById(R.id.aboutRecycView)

        for (i in 1..5) {
            aboutList?.add("")
        }
        aboutAdapter = AboutAdapter(aboutList!!, this, this)
        aboutRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        aboutRecycView?.adapter = aboutAdapter

        ivBack?.setOnClickListener(this)
        tvTitle?.text = "ParGo"
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }
}