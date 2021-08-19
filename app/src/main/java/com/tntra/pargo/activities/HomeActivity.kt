package com.tntra.pargo.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.JsonObject
import com.tntra.pargo.R
import com.tntra.pargo.activities.ContentDetailActivity
import com.tntra.pargo.activities.NotificationActivity
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.RetrofitCommonClass.CommonRetrofit.context
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.common.replaceFragmenty
import com.tntra.pargo.fragment.*
import com.tntra.pargo.viewmodel.ContentViewModel
import com.tntra.pargo.viewmodel.LoginActivityViewModel
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel

class HomeActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    //    private var btnCreateSession: Button? = null
//    private var btnAcceptRejectRequest: Button? = null
//    private var btnCreateRequest: Button? = null
//    private var btnLogout: Button? = null
//    private var btnContentList: Button? = null
//    private var btnContentShow: Button? = null
//    private var btnLikeUnlike: Button? = null

    private lateinit var collabSessionviewModel: CollabSessionviewModel
    private lateinit var contentViewModel: ContentViewModel
    private var prefManager: PrefManager? = null
    private lateinit var loginViewModel: LoginActivityViewModel

    private var ivNotification: ImageView? = null
    private var ivHomeTab: ImageView? = null
    private var ivSearchTab: ImageView? = null
    private var ivCollabTab: ImageView? = null
    private var ivMessageTab: ImageView? = null
    private var ivProfileTab: ImageView? = null
    private var ivDrawer: ImageView? = null
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        WindowUtil.hideWindowStatusBar(getWindow());
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)
        prefManager = context.let { PrefManager(it) }

        initView()

        replaceFragmenty(
                fragment = HomeFragment(),
                allowStateLoss = true,
                containerViewId = R.id.mainContent
        )

        callApiToSaveToken()

        if (intent != null) {
            if (intent.hasExtra("type")) {
                type = intent.getStringExtra("type")!!
                if (type.equals("join_collab")) {
                    ivMessageTab?.performClick()
                }
            }
        }
    }

    private fun callApiToSaveToken() {
        try {
            FirebaseInstanceId.getInstance().instanceId
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful) {
                            return@OnCompleteListener
                        }

                        // Get new Instance ID token
                        val token = task.result!!.token


                        Log.i("token", "+++" + token)


                        addFCMToken(token)

                    })


        } catch (e: Exception) {
            e.printStackTrace()

        }


    }

    private fun addFCMToken(token: String) {
        val json = JsonObject()
        json.addProperty("registration_token", token)
        Log.e("TAGG", "addFCMToken: " + json)
        loginViewModel.callApiStoreFCMToken(prefManager?.getAccessToken()!!, json)
        loginViewModel.getStoreFCMToken()?.observe(this, Observer {
            if (it != null) {
                if (it.success) {
                    Log.e("TAGG", "addFCMToken: " + it)
                }
            }
        })
    }

    private fun initView() {
        ivDrawer = findViewById(R.id.ivDrawer)
        ivNotification = findViewById(R.id.ivNotification)
        ivHomeTab = findViewById(R.id.ivHomeTab)
        ivCollabTab = findViewById(R.id.ivCollabTab)
        ivMessageTab = findViewById(R.id.ivMessageTab)
        ivProfileTab = findViewById(R.id.ivProfileTab)
        ivSearchTab = findViewById(R.id.ivSearchTab)

        ivNotification?.setOnClickListener(this)
        ivHomeTab?.setOnClickListener(this)
        ivProfileTab?.setOnClickListener(this)
        ivMessageTab?.setOnClickListener(this)
        ivSearchTab?.setOnClickListener(this)
        ivCollabTab?.setOnClickListener(this)
        ivDrawer?.setOnClickListener(this)

//        callApiContentList()
//        callApiCollabRooms()
    }


    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.ivNotification -> {
                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)
            }
            R.id.ivHomeTab -> {
                replaceFragmenty(
                        fragment = HomeFragment(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                )
            }
            R.id.ivProfileTab -> {
                replaceFragmenty(
                        fragment = ProfileFragment(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                )
            }
            R.id.ivMessageTab -> {
                replaceFragmenty(
                        fragment = MessageFragment(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                )
            }
            R.id.ivSearchTab -> {
                replaceFragmenty(
                        fragment = SearchFragment(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                )
            }
            R.id.ivCollabTab -> {
                replaceFragmenty(
                        fragment = CollabFragment(),
                        allowStateLoss = true,
                        containerViewId = R.id.mainContent
                )
            }
            R.id.ivDrawer -> {
//                val intent = Intent(this, ContentDetailActivity::class.java)
//                startActivity(intent)
            }
        }
    }

    private fun callApiContentShow() {
        Common.showLoader(this)

        contentViewModel.contentShowApi(prefManager?.getAccessToken()!!, prefManager?.getUserId()!!)
        contentViewModel.getContentShow()?.observe(this, {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "Content Show", "" + it, false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.message, false, false)
            }
        })
    }

    private fun callApiLikeUnlike() {
        Common.showLoader(this)

        contentViewModel.likeUnLikeApi(prefManager?.getAccessToken()!!, prefManager?.getUserId()!!)
        contentViewModel.getLikeUnLike()?.observe(this, {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "Like UnLike", "" + it, false, false)
            } else {
                Common.showDialogue(this, "Oops!", it.message, false, false)
            }
        })
    }

    private fun callApiContentList() {
        Common.showLoader(this)

        contentViewModel.contentListApi(prefManager?.getAccessToken()!!)
        contentViewModel.getContent()?.observe(this, Observer {
            Common.hideLoader()
            if (it.success) {
                Common.showDialogue(this, "Content List", "" + it, false, false)
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

    fun showDialogue(
            activity: Activity,
            title: String,
            message: String,
            islogout: Boolean
    ) {
        val builder = AlertDialog.Builder(activity).create()
        builder.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        builder?.window?.setLayout(width, height)
        builder.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

        val root = LayoutInflater.from(activity).inflate(R.layout.common_dialogue_layout_two, null)

        val btnYes = root.findViewById<Button>(R.id.btnOk)
        val btnNo = root.findViewById<Button>(R.id.btnNo)
        val ivClose = root.findViewById<ImageView>(R.id.ivClose)
        val tv_message = root.findViewById<TextView>(R.id.tv_message)
        val tvTitleText = root.findViewById<TextView>(R.id.tvTitleText)
        tvTitleText?.text = title
        tv_message.text = message
        btnYes.setOnClickListener {
            builder.dismiss()

            if (islogout) {
                callLogoutApi()
            }
        }

        btnNo.setOnClickListener {

            builder.dismiss()
        }

        ivClose.setOnClickListener {
            builder?.dismiss()
        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

    private fun callLogoutApi() {
        Common.showLoader(this)
        loginViewModel.callLogout(prefManager?.getAccessToken()!!)
        loginViewModel.getLogout()?.observe(this, Observer {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {

                    showDialogue(this, "Logout", it.message, true)

                } else {
                    Common.hideLoader()
//                    if (it.error != null && it.error.get(0).message != null) {
//                    }
                    showDialogue(this, "Oops!", it.message, false)
                }
            }
        })
    }

    override fun onPositionClick(variable: Int, check: Int) {
        TODO("Not yet implemented")
    }

}