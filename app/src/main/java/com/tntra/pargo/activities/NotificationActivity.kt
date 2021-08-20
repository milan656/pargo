package com.tntra.pargo.activities

import android.app.Activity
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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
import com.tntra.pargo.model.notification.Notification
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel
import java.text.SimpleDateFormat

class NotificationActivity : AppCompatActivity(), onClickAdapter {

    private var mAdapter: LeadHistoryAdapter? = null
    var historyDataList: ArrayList<Notification> = ArrayList()
    var simpleDateFormat: SimpleDateFormat? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var collabSessionviewModel: CollabSessionviewModel
    private var prefManager: PrefManager? = null


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

        /*for (i in 0..11) {

            var dashboardModel: Notification? = null
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
                dashboardModel = Notification(
                        null, "", "", "Neha karkae", "Sent you a request to collab", dateString, "uuid", "it.data.get(i).date_formated",
                        startDate,
                        startDate, false
                )
            } else if (i == 3) {
                dateString = "2021-08-22"
                dashboardModel = Notification(
                        null, "", "", "John Due", "Starting following you", dateString, "uuid", "it.data.get(i).date_formated",
                        startDate,
                        startDate, true
                )
            } else {
                dateString = "2021-09-01"
                dashboardModel = Notification(
                        null, "", "", "Jane Doe", "Accepted your invite", dateString, "uuid", "it.data.get(i).date_formated",
                        startDate,
                        startDate, true
                )
            }

            historyDataList.add(dashboardModel)
        }*/

        collabSessionviewModel.callApiNotificationlist(prefManager?.getAccessToken()!!)
        collabSessionviewModel.getNotificationList().observe(this, {
            if (it != null) {
                if (it.success) {
                    historyDataList.clear()

                    for (i in it.notifications.indices) {

                        val dateString: String = it.notifications.get(i).attributes.created_at
                        /*if (i == 0 || i == 1 || i == 2) {
                            dateString = "2021-08-03"
                        } else if (i == 3 || i == 4) {
                            dateString = "2021-08-22"
                        } else {
                            dateString = "2021-09-01"
                        }*/
//                        val sdf = SimpleDateFormat("yyyy-MM-dd")
//                        val date = sdf.parse(dateString)

                        val date = Common.datefrom(dateString)
                        val startDate = 0L
                        Log.e("TAGNo", "setData: " + date)

                        val dashboardModel = Notification(
                                it.notifications.get(i).attributes, it.notifications.get(i).id, it.notifications.get(i).attributes.data.type, "Neha karkae", "Sent you a request to collab", dateString!!, "uuid", "it.data.get(i).date_formated",
                                startDate,
                                startDate, false
                        )
                        historyDataList.add(dashboardModel)
                    }
                    mAdapter?.notifyDataSetChanged()
                }
            }
        })
    }

    override fun onPositionClick(variable: Int, check: Int) {

        if (check == 0) {
//            accept
            callApiAcceptRejectReq("accepted", variable)
        } else {
//            reject
            callApiAcceptRejectReq("rejected", variable)
        }
    }

    private fun callApiAcceptRejectReq(status: String, variable: Int) {
        Common.showLoader(this)
        val main = JsonObject()
        val session = JsonObject()
        session.addProperty("status", status)
        main.add("member", session)

        Log.e("TAG", "callApiAcceptRejectReq: " + main.toString())

        collabSessionviewModel.collabAcceptReject(prefManager?.getAccessToken()!!, main, historyDataList.get(variable).attributes.data.member_id)
        collabSessionviewModel.getCollabAcceptReject()?.observe(this, Observer {
            Common.hideLoader()
            if (it.success) {
                showDialogue(this, "Collab Request Status", "Collab request updated successfully", false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.errors, false, false)
            }
        })
    }

    fun showDialogue(
            activity: Activity,
            title: String,
            message: String,
            isBackPressed: Boolean,
            isnavigate: Boolean
    ) {
        val builder = AlertDialog.Builder(activity).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(activity).inflate(R.layout.common_dialogue_layout, null)

        val btnYes = root.findViewById<Button>(R.id.btnOk)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tv_message.text = message
        btnYes.setOnClickListener {
            builder.dismiss()
            setData()
        }

        ivClose.setOnClickListener {
            builder?.dismiss()
        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }
}
