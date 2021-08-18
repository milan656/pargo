package com.tntra.pargo.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tntra.pargo.R
import com.tntra.pargo.adapter.EventAdapter
import com.tntra.pargo.adapter.ExploreGeneresAdapter
import com.tntra.pargo.adapter.TreadingContentAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.custom.HeightWrappingViewPager
import com.tntra.pargo.model.generes.Genre
import com.tntra.pargo.model.treading_content.Content
import com.tntra.pargo.viewmodel.ContentViewModel
import com.tntra.pargo.viewmodel.LoginActivityViewModel
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel
import com.tntra.pargo.viewmodel.generes.GeneresViewModel
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment(), onClickAdapter {

    private lateinit var collabSessionviewModel: CollabSessionviewModel
    private lateinit var generesViewModel: GeneresViewModel
    private lateinit var contentViewModel: ContentViewModel
    private var prefManager: PrefManager? = null
    private lateinit var loginViewModel: LoginActivityViewModel
    var exoPlayerView: PlayerView? = null
    var eventRecycView: RecyclerView? = null
    var contentRecycView: RecyclerView? = null
    private var treadingContentAdapter: TreadingContentAdapter? = null
    var exploreGeneresRecycView: RecyclerView? = null
    var treadingContentPage = 1
    var isDataFinishTreading = false

    var exploreGenList: ArrayList<Genre>? = ArrayList()
    var exploreGeneresAdapter: ExploreGeneresAdapter? = null

    // creating a variable for exoplayer
    var treadingContentList: ArrayList<Content>? = ArrayList()
    var liveEventList: ArrayList<String>? = ArrayList()

    private var param1: String? = null
    private var param2: String? = null

    private var view_pager: HeightWrappingViewPager? = null
    private var tab_layout: TabLayout? = null
    private var dots_indicator: DotsIndicator? = null
    var timer: Timer? = null
    private var tvNoContentFound: TextView? = null
    private var nestedScrollHomePage: NestedScrollView? = null
    private var homeSwipe_refresh_layout: SwipeRefreshLayout? = null
    private var pagerAdapter:ViewPagerAdapter?=null

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        generesViewModel = ViewModelProviders.of(this).get(GeneresViewModel::class.java)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)
        prefManager = context?.let { PrefManager(it) }
        initView(view)
        Log.e("TAGG", "onCreateView: home")
        return view
    }

    private fun initView(view: View) {
        homeSwipe_refresh_layout = view.findViewById(R.id.homeSwipe_refresh_layout)
        nestedScrollHomePage = view.findViewById(R.id.nestedScrollHomePage)
        tvNoContentFound = view.findViewById(R.id.tvNoContentFound)
        dots_indicator = view.findViewById(R.id.dots_indicator)
        tab_layout = view.findViewById(R.id.tab_layout)
        view_pager = view.findViewById(R.id.view_pager)
        pagerAdapter = ViewPagerAdapter(getChildFragmentManager())
        // add the fragments

        exoPlayerView = view.findViewById(R.id.idExoPlayerVIew);
        eventRecycView = view.findViewById(R.id.eventRecycView);
        contentRecycView = view.findViewById(R.id.contentRecycView);
        exploreGeneresRecycView = view.findViewById(R.id.exploreGeneresRecycView);

        tab_layout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.e("TAGG", "onAdapterChanged: " + tab?.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        activity?.let {
            exploreGeneresAdapter = ExploreGeneresAdapter(exploreGenList!!, it, this)
        }
        exploreGeneresRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        exploreGeneresRecycView?.adapter = exploreGeneresAdapter

        for (i in 1..5) {
            liveEventList?.add("item")
//            treadingContentList?.add("")
        }


        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels

        eventRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        eventRecycView?.adapter = context?.let { EventAdapter(liveEventList, it, this, width) }
        contentRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        treadingContentAdapter = context?.let { TreadingContentAdapter(treadingContentList, it, this) }
        contentRecycView?.adapter = treadingContentAdapter

        getShowTopContentApi(pagerAdapter!!)
        getTreadingContentApiList(true)
        getGeneresList()

        nestedScrollHomePage?.viewTreeObserver
                ?.addOnScrollChangedListener {
                    if (nestedScrollHomePage?.getChildAt(0)?.bottom!! <= nestedScrollHomePage?.height!! + nestedScrollHomePage?.scrollY!!) {
                        //scroll view is at bottom
                        Log.e("TAG", "initView: at bottom")
                        if (!isDataFinishTreading) {
                            treadingContentPage += 1
                            getTreadingContentApiList(false)
                        }
                    }
                }

        homeSwipe_refresh_layout?.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                this@HomeFragment.onRefresh()
            }
        })
    }

    private fun onRefresh() {
        getShowTopContentApi(pagerAdapter!!)
        getTreadingContentApiList(true)
        getGeneresList()

        homeSwipe_refresh_layout?.post(Runnable { homeSwipe_refresh_layout?.isRefreshing = false })

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getShowTopContentApi(adapter: ViewPagerAdapter) {
        context?.let { Common.showLoader(it) }
        contentViewModel.topLatestContentApi(prefManager?.getAccessToken()!!,
                "timeline", "top_five")
        contentViewModel.getTopLatestContent()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {

                    for (i in 0 until it.contents.size) {
                        val gson = Gson()
                        val Json = gson.toJson(it.contents.get(i))
                        adapter.addFragment(PagerFragment.newInstance("", Json), "Page 1")
                    }

                    // Set the adapter
                    view_pager?.setAdapter(adapter)
                    tab_layout?.setupWithViewPager(view_pager, true);
                    dots_indicator?.setViewPager(view_pager!!)
                    view_pager?.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    val timerTask: TimerTask = object : TimerTask() {
                        override fun run() {
                            view_pager?.post({ view_pager?.setCurrentItem((view_pager?.getCurrentItem()!! + 1) % it.contents.size) })
                        }
                    }
                    timer = Timer()
                    timer?.schedule(timerTask, 3000, 3000)
                }
            }
        })
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getTreadingContentApiList(isclear: Boolean) {
        context?.let { Common.showLoader(it) }
        contentViewModel.treadingContentApi(prefManager?.getAccessToken()!!,
                "timeline", treadingContentPage, "latest")
        contentViewModel.getTreadingContent()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    if (isclear) {
                        treadingContentList?.clear()
                    }

                    if (it.contents.size == 0) {
                        isDataFinishTreading = true
                    }
                    treadingContentList?.addAll(it.contents)
                    treadingContentAdapter?.notifyDataSetChanged()

                    if (treadingContentList?.size!! > 0) {
                        tvNoContentFound?.visibility = View.GONE
                    } else {
                        tvNoContentFound?.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getGeneresList() {

        Log.e("TAGG", "getGeneresList: " + prefManager?.getAccessToken())
        context?.let { generesViewModel.callApiGeneresList(prefManager?.getAccessToken()!!, it) }

        generesViewModel.getGeneresList()?.observe(this, Observer {

            if (it != null) {
                if (it.success) {
                    exploreGenList?.clear()
                    exploreGenList?.addAll(it.genres)
                    exploreGeneresAdapter?.notifyDataSetChanged()
                }
            }
        })


    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onPositionClick(variable: Int, check: Int) {


    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
            FragmentStatePagerAdapter(manager) {
        private val mFragmentList = java.util.ArrayList<Fragment>()
        private val mFragmentTitleList = java.util.ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("TAGG", "onResume: home")
    }
}