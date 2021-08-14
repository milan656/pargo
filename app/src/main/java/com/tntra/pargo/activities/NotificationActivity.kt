package com.tntra.pargo.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderDecoration
import com.google.gson.JsonObject
import com.tntra.pargo.R
import com.tntra.pargo.adapter.LeadHistoryAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.DashboardModel
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel
import java.text.SimpleDateFormat

class NotificationActivity : AppCompatActivity(), onClickAdapter {

    private var mAdapter: LeadHistoryAdapter? = null
    var historyDataList: ArrayList<DashboardModel> = ArrayList()
    var simpleDateFormat: SimpleDateFormat? = null
    private var recyclerView: RecyclerView? = null
    private var prefManager: PrefManager? = null
    private lateinit var collabSessionviewModel: CollabSessionviewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = PrefManager(this)
        initView()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.recyclerView)
        mAdapter = LeadHistoryAdapter(this, historyDataList, this)
        val decor = StickyHeaderDecoration(mAdapter)

        // use a linear layout manager
        val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView?.setLayoutManager(layoutManager)
        recyclerView?.setAdapter(mAdapter)
        recyclerView?.addItemDecoration(decor)
        mAdapter?.onclick = this

        setData()
    }

    private fun setData() {

        for (i in 0..11) {

            var dashboardModel: DashboardModel? = null
            var dateString: String? = ""
            if (i == 0 || i == 1 || i == 2) {
                dateString = "2021-08-03"
            } else if (i == 3 || i == 4) {
                dateString = "2021-08-22"
            } else {
                dateString = "2021-09-01"
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val date = sdf.parse(dateString)

            val startDate = date.time

            if (i == 0) {
                dateString = "2021-08-03"
                dashboardModel = DashboardModel(
                        "Neha karkae", "Sent you a request to collab", dateString, "uuid", "it.data.get(i).date_formated",
                        1, 2, 3, 4, startDate,
                        startDate, false
                )
            } else if (i == 3) {
                dateString = "2021-08-22"
                dashboardModel = DashboardModel(
                        "John Due", "Starting following you", dateString, "uuid", "it.data.get(i).date_formated",
                        1, 2, 3, 4, startDate,
                        startDate, true
                )
            } else {
                dateString = "2021-09-01"
                dashboardModel = DashboardModel(
                        "Jane Doe", "Accepted your invite", dateString, "uuid", "it.data.get(i).date_formated",
                        1, 2, 3, 4, startDate,
                        startDate, true
                )
            }

            historyDataList.add(dashboardModel)
        }

        mAdapter?.notifyDataSetChanged()
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 0) {
//            accept
            callApiAcceptRejectReq("accepted")
        } else {
//            reject
            callApiAcceptRejectReq("rejected")
        }
    }

    private fun callApiAcceptRejectReq(status: String) {
        Common.showLoader(this)
        val main = JsonObject()
        val session = JsonObject()
        session.addProperty("status", status)
        main.add("collab_request", session)

        Log.e("TAG", "callApiAcceptRejectReq: " + main.toString())

        collabSessionviewModel.collabAcceptReject(prefManager?.getAccessToken()!!, main, prefManager?.getUserId()!!)
        collabSessionviewModel.getCollabAcceptReject()?.observe(this, Observer {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "collab Accept Reject Request", "" + it, false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.message, false, false)
            }
        })
    }
}
