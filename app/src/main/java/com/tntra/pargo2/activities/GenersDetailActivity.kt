package com.tntra.pargo2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo2.R
import com.tntra.pargo2.adapter.RecommandCreatorsAdapter
import com.tntra.pargo2.adapter.RecommandedContentAdapter
import com.tntra.pargo2.common.onClickAdapter
import com.tntra.pargo2.model.creatorListsearch.CreatorsListMdel

class GenersDetailActivity : AppCompatActivity(), onClickAdapter {

    var recommandCreatorsList: ArrayList<CreatorsListMdel>? = ArrayList()
    var recommandedCreatorsRecycView: RecyclerView? = null
    var recommandCreatorsAdapter: RecommandCreatorsAdapter? = null
    private var tvGenersName: TextView? = null
    private var tvTitle: TextView? = null
    private var tvNoCreatorGeners: TextView? = null
    private var contentGeneresRecycView:RecyclerView?=null
    var list: ArrayList<String>? = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geners_detail)

        initView()
    }

    private fun initView() {

        tvGenersName = findViewById(R.id.tvGenersName);
        contentGeneresRecycView = findViewById(R.id.contentGeneresRecycView);
        tvNoCreatorGeners = findViewById(R.id.tvNoCreatorGeners);
        tvTitle = findViewById(R.id.tvTitle);
        recommandedCreatorsRecycView = findViewById(R.id.latestCreatorsRecycView);
        for (i in 1..4) {
            recommandCreatorsList?.add(CreatorsListMdel("http://placehold.it/120x120&text=image1",
                    "Creator Name " + i))
        }
        recommandCreatorsAdapter = RecommandCreatorsAdapter(recommandCreatorsList!!, this, this)
        recommandedCreatorsRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        recommandedCreatorsRecycView?.adapter = recommandCreatorsAdapter

        if (intent != null) {
            tvGenersName?.text = intent.getStringExtra("geners")
        }
        tvTitle?.text = "PARGO"

        for (i in 1..5) {
            list?.add("item")
        }

        contentGeneresRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        contentGeneresRecycView?.adapter = this.let { RecommandedContentAdapter(list, it, this) }

    }

    override fun onPositionClick(variable: Int, check: Int) {

    }
}