package com.tntra.pargo2.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
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
import com.tntra.pargo2.R
import com.tntra.pargo2.adapter.FollowersListAdapter
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.OnBottomReachedListener
import com.tntra.pargo2.common.PrefManager
import com.tntra.pargo2.common.onClickAdapter
import com.tntra.pargo2.model.followers.Follow
import com.tntra.pargo2.model.followers.FollowSelected
import com.tntra.pargo2.viewmodel.collab.CollabSessionviewModel

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

    private var followersCkeckedList: ArrayList<Follow>? = ArrayList()
    private var followingsCkeckedList: ArrayList<Follow>? = ArrayList()
    private var ivBack: ImageView? = null
    private var tvNoData: TextView? = null

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
        tvNoData = findViewById(R.id.tvNoData)
        ivBack = findViewById(R.id.ivBack)
        followersRecycView = findViewById(R.id.followersRecycView)
        btnSubmit = findViewById(R.id.btnSubmit)
        llmainContent = findViewById(R.id.llmainContent)
        btnSubmit?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)

        viewPager = findViewById(R.id.viewpager) as ViewPager

        setupViewPager(viewPager!!)
        viewPager?.setCurrentItem(0)
        tabs = findViewById(R.id.tabs) as TabLayout
        tabs?.setupWithViewPager(viewPager)
        tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                addSelectedList()
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

            if (it != null) {
                if (it.success) {
                    if (isClear) {
                        followersList?.clear()
                    }
                    isdataFinish = it.follows.size == 0
                    followersList?.addAll(it.follows)

                    if (listType.equals("followings")) {
                        if (followingsCkeckedList?.size!! > 0) {

                            for (i in it.follows.indices) {
                                for (j in followingsCkeckedList?.indices!!) {
                                    try {
                                        Log.e("TAG", "getFollowersApi: " + followersList?.get(i)?.attributes?.name + " : " + followersList?.get(i)?.id + " == " + followersCkeckedList?.get(j)?.id)
                                        if (followingsCkeckedList?.get(j)?.id == followersList?.get(i)?.id) {
                                            it.follows.get(i).isChecked = true
                                        } else {
                                            it.follows.get(i).isChecked = false
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                    } else {
                        if (followersCkeckedList?.size!! > 0) {
                            Log.e("TAGGK1", "getFollowersApi: " + followersCkeckedList)
                            for (i in it.follows.indices) {
                                for (j in followersCkeckedList?.indices!!) {
                                    try {
                                        Log.e("TAG", "getFollowersApi: " + followersList?.get(i)?.attributes?.name + " : " + followersList?.get(i)?.id + " == " + followersCkeckedList?.get(j)?.id)
                                        if (followersCkeckedList?.get(j)?.id == followersList?.get(i)?.id) {
                                            it.follows.get(i).isChecked = true
                                        } else {
                                            it.follows.get(i).isChecked = false
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }
                    }

                    if (followersList?.size == 0) {
                        tvNoData?.visibility = View.VISIBLE
                    } else {
                        tvNoData?.visibility = View.GONE
                    }
                    followersListAdapter?.notifyDataSetChanged()
                    Common.hideLoader()
                } else {
                    Common.hideLoader()
                }
            } else {
                Common.hideLoader()
            }
        })
    }

    fun addSelectedList() {
        try {
            if (followersListAdapter?.getSelected() != null && followersListAdapter?.getSelected()?.size!! > 0) {

                if (listType.equals("followings")) {
                    followingsCkeckedList?.clear()
                } else {
                    followersCkeckedList?.clear()
                }
                for (i in 0 until followersListAdapter?.getSelected()?.size!!) {
                    if (listType.equals("followings")) {
                        followersListAdapter?.getSelected()?.get(i)?.isChecked = true
//                            if (!followingsCkeckedList?.contains(followersListAdapter?.getSelected()?.get(i)?.id!!)!!) {
                        followingsCkeckedList?.add(Follow(followersListAdapter?.getSelected()?.get(i)?.attributes!!, followersListAdapter?.getSelected()?.get(i)?.id!!,
                                followersListAdapter?.getSelected()?.get(i)?.type!!, followersListAdapter?.getSelected()?.get(i)?.isChecked!!))
//                            }

                    } else {
                        followersListAdapter?.getSelected()?.get(i)?.isChecked = true
//                            if (!followersCkeckedList?.contains(followersListAdapter?.getSelected()?.get(i)?.id)!!) {
                        followersCkeckedList?.add(Follow(followersListAdapter?.getSelected()?.get(i)?.attributes!!, followersListAdapter?.getSelected()?.get(i)?.id!!,
                                followersListAdapter?.getSelected()?.get(i)?.type!!, followersListAdapter?.getSelected()?.get(i)?.isChecked!!))
//                            }

                    }
                }

            }
            Log.e("TAGGk", "getFollowersApi: " + followersCkeckedList?.size)
            Log.e("TAGGk", "getFollowersApi: " + followingsCkeckedList?.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Fragment(), "Followers")
        adapter.addFragment(Fragment(), "Following")
        viewPager.adapter = adapter
    }

    override fun onPositionClick(variable: Int, check: Int) {

        Log.e("TAGpos", "onPositionClick: " + followersList?.get(variable)?.id)
        Log.e("TAGpos", "onPositionClick: " + followersList?.get(variable)?.isChecked)


    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnSubmit -> {
                val mainJson = JsonObject()
                val arr = JsonArray();
                var json: JsonObject? = null

                if (followingsCkeckedList?.size!! > 0) {

//                    val distinct = followingsCkeckedList?.toSet()?.toList();
                    for (i in followingsCkeckedList?.indices!!) {
                        json = JsonObject()
                        json.addProperty("name", followingsCkeckedList?.get(i)?.attributes?.name)
                        json.addProperty("id", followingsCkeckedList?.get(i)?.id)
                        arr.add(json)
                    }
                }

                Log.e("TAGG", "onClick: " + followingsCkeckedList?.size)
                Log.e("TAGG", "onClick: " + arr.size())

                if (followersCkeckedList?.size!! > 0) {
//                    val distinct = followersCkeckedList?.toSet()?.toList();
                    for (i in followersCkeckedList?.indices!!) {
                        json = JsonObject()
                        json.addProperty("name", followersCkeckedList?.get(i)?.attributes?.name)
                        json.addProperty("id", followersCkeckedList?.get(i)?.id)
                        arr.add(json)
                    }
                }

                Log.e("TAG", "onClick: " + followersCkeckedList?.size)
                Log.e("TAG", "onClick: " + arr)
                mainJson.add("followers", arr)
                Log.e("TAGG", "onClick: " + mainJson)

                if (arr.size() == 0) {
                    Toast.makeText(this, "Please select followers / followings ", Toast.LENGTH_SHORT).show()
                    return
                }
                if (arr.size() > 4) {
                    Toast.makeText(this, "You can select maximum 4 ", Toast.LENGTH_SHORT).show()
                    return
                }

                val intent = Intent(this, SendRequestActivity::class.java)
                intent.putExtra("followers", mainJson.toString())
                intent.putExtra("collabType", collabType)
                startActivity(intent)

                /* Log.e("TAG", "onClick: " + followersCkeckedList)
                Log.e("TAG", "onClick: " + followersCkeckedList?.distinctBy { it.id })

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
                }*/

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

