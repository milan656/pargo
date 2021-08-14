package com.tntra.pargo.fragment

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tntra.pargo.R
import com.tntra.pargo.activities.EventActivity
import com.tntra.pargo.adapter.LatestContentAdapter
import com.tntra.pargo.adapter.UpcomingEventsAdapter
import com.tntra.pargo.common.onClickAdapter
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment(), onClickAdapter {
    private var param1: String? = null
    private var param2: String? = null

    private var upcomingRecycView: RecyclerView? = null
    private var latestContentRecycView: RecyclerView? = null
    private var upcomingEventsAdapter: UpcomingEventsAdapter? = null
    private var latestContentAdapter: LatestContentAdapter? = null
    private var upcomingList: ArrayList<String>? = ArrayList()
    private var latestContentList: ArrayList<String>? = ArrayList()

    private var ivCoverpic:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        ivCoverpic = view?.findViewById(R.id.ivCoverpic)
        upcomingRecycView = view?.findViewById(R.id.upcomingRecycView)
        latestContentRecycView = view?.findViewById(R.id.latestContentRecycView)

        for (i in 1..5) {
            upcomingList?.add("item")
            latestContentList?.add("item")
        }

        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        upcomingEventsAdapter = activity?.let {
            UpcomingEventsAdapter(upcomingList!!, it, this, width)
        }
        upcomingRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        upcomingRecycView?.adapter = upcomingEventsAdapter
        upcomingEventsAdapter?.onpositionClick = this

        latestContentAdapter = activity?.let {
            LatestContentAdapter(latestContentList!!, it, this)
        }
        latestContentRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        latestContentRecycView?.adapter = latestContentAdapter

        try {
            context?.let { Glide.with(it).load("http://placehold.it/120x120&text=image1").into(ivCoverpic!!) }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 10) {
            val intent = Intent(activity, EventActivity::class.java)
            startActivity(intent)
        }
    }
}