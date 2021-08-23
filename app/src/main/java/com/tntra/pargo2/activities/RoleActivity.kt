package com.tntra.pargo2.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonObject
import com.tntra.pargo2.R
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.PrefManager
import com.tntra.pargo2.viewmodel.collab.CollabSessionviewModel
import io.agora.rtc.Constants

class RoleActivity : RtcBaseActivity(), View.OnClickListener {

    private var selected = "host"
    private var prefManager: PrefManager? = null
    private var broadcaster_layout: RelativeLayout? = null
    private var audience_layout: RelativeLayout? = null
    private var btnCreateSession: Button? = null
    private var btnAcceptRejectRequest: Button? = null
    private var btnCreateRequest: Button? = null
    private lateinit var collabSessionviewModel: CollabSessionviewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_role)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = PrefManager(this)
        broadcaster_layout = findViewById(R.id.broadcaster_layout)
        audience_layout = findViewById(R.id.audience_layout)

        btnCreateSession = findViewById(R.id.btnCreateSession)
        btnCreateRequest = findViewById(R.id.btnCreateRequest)
        btnAcceptRejectRequest = findViewById(R.id.btnAcceptRejectRequest)

        rtcEngine().registerLocalUserAccount(getString(R.string.private_app_id), "milan.parmar")
        broadcaster_layout?.setOnClickListener({ v -> onJoinAsBroadcaster(v) })
        audience_layout?.setOnClickListener({ v -> onJoinAsAudience(v) })

        btnCreateSession?.setOnClickListener(this)
        btnCreateRequest?.setOnClickListener(this)
        btnAcceptRejectRequest?.setOnClickListener(this)

    }

    private fun callApiCreateSession() {
        Common.showLoader(this)
        val main = JsonObject()
        val session = JsonObject()
        session.addProperty("collab_room_id", 1)
        session.addProperty("channel_name", "testchannel")
        session.addProperty("uid", "10001")
        main.add("collab_session", session)

        Log.e("TAG", "callApiCreateSession: " + main.toString())

        collabSessionviewModel.collabSessionApi(prefManager?.getAccessToken()!!, main)
        collabSessionviewModel.getCollabSession()?.observe(this, Observer {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "collab session", "" + it.collab_session, false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.message, false, false)
            }
        })
    }

    override fun onGlobalLayoutCompleted() {
//        RelativeLayout layout = findViewById(R.id.role_title_layout);
//        RelativeLayout.LayoutParams params =
//                (RelativeLayout.LayoutParams) layout.getLayoutParams();
//        params.height += mStatusBarHeight;
//        layout.setLayoutParams(params);
//
//        params = (RelativeLayout.LayoutParams) layout.getLayoutParams();
//        params.topMargin = (mDisplayMetrics.heightPixels -
//                layout.getMeasuredHeight()) * 3 / 7;
//        layout.setLayoutParams(params);
    }

    fun onJoinAsBroadcaster(view: View?) {
        selected = "host"
        gotoLiveActivity(Constants.CLIENT_ROLE_BROADCASTER)
    }

    fun onJoinAsAudience(view: View?) {
        selected = "client"
        gotoLiveActivity(Constants.CLIENT_ROLE_AUDIENCE)
    }

    private fun gotoLiveActivity(role: Int) {
        val intent = Intent(intent)
        intent.putExtra(com.tntra.pargo2.Constants.KEY_CLIENT_ROLE, role)
        intent.setClass(applicationContext, LiveActivity::class.java)
        intent.putExtra("selected", selected)
        startActivity(intent)
    }

    fun onBackArrowPressed(view: View?) {
        finish()
    }


    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.btnAcceptRejectRequest -> {

                callApiAcceptRejectReq()
            }
            R.id.btnCreateSession -> {
                callApiCreateSession()
            }
            R.id.btnCreateRequest -> {
                callApicreateRequest()
            }
        }
    }

    private fun callApiAcceptRejectReq() {
        Common.showLoader(this)
        val main = JsonObject()
        val session = JsonObject()
        session.addProperty("status", "rejected")
        main.add("collab_request", session)

        Log.e("TAG", "callApiAcceptRejectReq: " + main.toString())

        collabSessionviewModel.collabAcceptReject(prefManager?.getAccessToken()!!, main, 5)
        collabSessionviewModel.getCollabAcceptReject()?.observe(this, Observer {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "collab Accept Reject Request", "" + it, false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.message, false, false)
            }
        })
    }

    private fun callApicreateRequest() {
        Common.showLoader(this)
        val main = JsonObject()
        val session = JsonObject()
        session.addProperty("sender_id", prefManager?.getUserId())
        session.addProperty("receiver_id", 5)
        main.add("collab_request", session)

        Log.e("TAG", "callApicreateRequest: " + main.toString())

        collabSessionviewModel.collabRequestApi(prefManager?.getAccessToken()!!, main)
        collabSessionviewModel.getCollabReq()?.observe(this, Observer {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "collab Request", "" + it, false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.message, false, false)
            }
        })
    }
}