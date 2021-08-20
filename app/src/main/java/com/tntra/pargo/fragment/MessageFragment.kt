package com.tntra.pargo.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.activities.MessageDetailActivity
import com.tntra.pargo.adapter.FollowersListAdapter
import com.tntra.pargo.adapter.MessageListAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.FollowerModel
import com.tntra.pargo.model.collabRoomList.CollabRoom
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MessageFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var messageListAdapter: MessageListAdapter? = null
    private var messageRecyclerView: RecyclerView? = null
    private var roomList: ArrayList<CollabRoom>? = ArrayList()
    private var collabSessionviewModel: CollabSessionviewModel? = null
    private var prefManager: PrefManager? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_message, container, false)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = context?.let { PrefManager(it) }
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        messageRecyclerView = view?.findViewById(R.id.messageRecycView)
        /*for (i in 0..6) {
            if (i == 0) {
                followersList?.add("Collab name 1")
            } else if (i == 1) {
                followersList?.add("Neha Karkae")
            } else {
                followersList?.add("Collab name" + i)
            }
        }*/
        messageListAdapter = context?.let { MessageListAdapter(roomList!!, it, this) }
        messageRecyclerView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        messageRecyclerView?.adapter = messageListAdapter

        getCollabRoomList()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getCollabRoomList() {
        context?.let { Common.showLoader(it) }
        collabSessionviewModel?.callApiCollabRoomList(prefManager?.getAccessToken()!!)
        collabSessionviewModel?.getCollabRoomList()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    roomList?.clear()
                    roomList?.addAll(it.collab_rooms)
                    Log.e("TAGF", "getCollabRoomList: " + roomList?.size)
                    messageListAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MessageFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 5) {
            val intent = Intent(context, MessageDetailActivity::class.java)
            intent.putExtra("name", roomList?.get(variable)?.attributes?.name)
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {

    }
}