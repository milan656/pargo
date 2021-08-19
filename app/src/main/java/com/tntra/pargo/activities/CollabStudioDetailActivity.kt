package com.tntra.pargo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tntra.pargo.R
import com.tntra.pargo.adapter.FollowersListAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.OnBottomReachedListener
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.followers.Follow
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel

class CollabStudioDetailActivity : AppCompatActivity(), onClickAdapter, View.OnClickListener, OnBottomReachedListener {

    private var followersListAdapter: FollowersListAdapter? = null
    private var followersRecycView: RecyclerView? = null
    private var followersList: ArrayList<Follow>? = ArrayList()
    private var btnSubmit: Button? = null

    private var llmainContent: LinearLayout? = null
    private var viewPager: ViewPager? = null
    private var tabs: TabLayout? = null
    private var prefManager: PrefManager? = null
    private lateinit var collabSessionviewModel: CollabSessionviewModel
    private var collabType: String = ""

    private var listType: String = ""
    private var isdataFinish = false
    private var followersPage = 1
    private var followingsPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collab_studio_detail)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = PrefManager(this)

        if (intent != null) {
            if (intent.hasExtra("collabType")) {
                collabType = intent.getStringExtra("collabType")!!
            }
        }

        listType = "followers"
        initView()
    }

    private fun initView() {
        followersRecycView = findViewById(R.id.followersRecycView)
        btnSubmit = findViewById(R.id.btnSubmit)
        llmainContent = findViewById(R.id.llmainContent)
        btnSubmit?.setOnClickListener(this)

        viewPager = findViewById(R.id.viewpager) as ViewPager

        setupViewPager(viewPager!!)
        viewPager?.setCurrentItem(0)
        tabs = findViewById(R.id.tabs) as TabLayout
        tabs?.setupWithViewPager(viewPager)
        tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text?.toString().equals("Following")) {
                    listType = "followings"
                    isdataFinish = false
                    followingsPage = 1
                    getFollowersApi(true)
                } else {
                    listType = "followers"
                    isdataFinish = false
                    followersPage = 1
                    getFollowersApi(true)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        getFollowersApi(true)
        followersListAdapter = FollowersListAdapter(followersList!!, this, this, this)
        followersRecycView?.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        followersRecycView?.adapter = followersListAdapter
        followersListAdapter?.onBottomReached = this
    }

    private fun getFollowersApi(isClear: Boolean) {

        Common.showLoader(this)
        if (listType.equals("followings")) {
            collabSessionviewModel.callApiFollowerslist(prefManager?.getAccessToken()!!, prefManager?.getUserId()!!, followingsPage, listType)
        } else {
            collabSessionviewModel.callApiFollowerslist(prefManager?.getAccessToken()!!, prefManager?.getUserId()!!, followersPage, listType)
        }

        collabSessionviewModel.getFollowers()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    if (isClear) {
                        followersList?.clear()
                    }
                    isdataFinish = it.follows.size == 0
                    followersList?.addAll(it.follows)
                    followersListAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Fragment(), "Followers")
        adapter.addFragment(Fragment(), "Following")
        viewPager.adapter = adapter
    }

    override fun onPositionClick(variable: Int, check: Int) {

    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.btnSubmit -> {
                val mainJson = JsonObject()
                val arr = JsonArray();
                var json: JsonObject? = null
                if (followersListAdapter?.getSelected()?.size!! > 0) {
                    val stringBuilder = StringBuilder()
                    for (i in 0 until followersListAdapter?.getSelected()?.size!!) {
                        stringBuilder.append(followersListAdapter?.getSelected()?.get(i)?.attributes?.name)
                        json = JsonObject()
                        json.addProperty("name", followersListAdapter?.getSelected()?.get(i)?.attributes?.name)
                        json.addProperty("id", followersListAdapter?.getSelected()?.get(i)?.id)
                        arr.add(json)
                    }

                    mainJson.add("followers", arr)
                    Log.e("TAGG", "onClick: " + mainJson)

                    if (followersListAdapter?.getSelected()?.size!! > 4) {
                        Toast.makeText(this, "You can select maximum 4 ", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val intent = Intent(this, SendRequestActivity::class.java)
                    intent.putExtra("followers", mainJson.toString())
                    intent.putExtra("collabType", collabType)
                    startActivity(intent)
//                    showToast(stringBuilder.toString().trim { it <= ' ' })
                } else {
                    Log.e("TAGG", "onClick: No selection")
//                    showToast("No Selection")
                }

            }
        }
    }

    internal inner class ViewPagerAdapter(manager: FragmentManager) :
            FragmentPagerAdapter(manager) {
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

    override fun onBottomReached(position: Int) {

        Log.e("TAGbott", "onBottomReached: " + followersList?.get(position)?.attributes?.name)
        Log.e("TAGbott", "onBottomReached: " + position)
        if (position >= 9) {
            if (!isdataFinish) {
                if (listType.equals("followings")) {
                    followingsPage += 1
                } else {
                    followersPage += 1
                }
                getFollowersApi(false)
            }
        }
    }
}

