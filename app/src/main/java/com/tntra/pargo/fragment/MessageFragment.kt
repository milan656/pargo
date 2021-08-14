package com.tntra.pargo.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tntra.pargo.R
import com.tntra.pargo.activities.MessageDetailActivity
import com.tntra.pargo.adapter.FollowersListAdapter
import com.tntra.pargo.adapter.MessageListAdapter
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.FollowerModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MessageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessageFragment : Fragment(), onClickAdapter, View.OnClickListener {
    private var followersListAdapter: MessageListAdapter? = null
    private var followersRecycView: RecyclerView? = null
    private var followersList: ArrayList<String>? = ArrayList()

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
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        followersRecycView = view?.findViewById(R.id.followersRecycView)
        for (i in 0..6) {
            if (i == 0) {
                followersList?.add("Collab name 1")
            } else if (i == 1) {
                followersList?.add("Neha Karkae")
            } else {
                followersList?.add("Collab name" + i)
            }

        }
        followersListAdapter = context?.let { MessageListAdapter(followersList!!, it, this) }
        followersRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        followersRecycView?.adapter = followersListAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MessageFragment.
         */
        // TODO: Rename and change types and number of parameters
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
            intent.putExtra("name", followersList?.get(variable))
            startActivity(intent)
        }
    }

    override fun onClick(v: View?) {

    }
}