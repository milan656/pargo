package com.tntra.pargo.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.tntra.pargo.R
import com.tntra.pargo.activity.HomeActivity
import com.tntra.pargo.adapter.SelectedFollowersAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.model.SelectedFollowrs
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SendRequestActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private lateinit var collabSessionviewModel: CollabSessionviewModel
    private var prefManager: PrefManager? = null
    private var tvSendReq: TextView? = null
    var followers: String? = ""
    var jsonObject: JsonObject = JsonObject();
    private var followersRecycView: RecyclerView? = null
    private var selectedFollowersAdapter: SelectedFollowersAdapter? = null
    private var followersList: ArrayList<SelectedFollowrs>? = ArrayList()
    private var receiver_ids: ArrayList<Int>? = ArrayList()
    private var ivBack: ImageView? = null
    private var edtCollabDescription: EditText? = null
    private var edtCollabName: EditText? = null
    private var collabType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request_acrivity)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = this.let { PrefManager(it) }
        try {
            if (intent != null) {

                if (intent != null) {
                    if (intent.hasExtra("collabType")) {
                        collabType = intent.getStringExtra("collabType")!!
                    }
                }
                followers = intent.getStringExtra("followers")
                Log.e("TAGG", "onCreate: " + followers)

                val obj = JSONObject(followers)
                val arr = obj.getJSONArray("followers")

                followersList?.clear()
                receiver_ids?.clear()
                for (i in 0 until arr.length()) {
                    followersList?.add(SelectedFollowrs(arr.getJSONObject(i).getString("name"),
                            arr.getJSONObject(i).getString("id")))
                    receiver_ids?.add(arr.getJSONObject(i).getString("id").toInt())
                }

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        initView()
    }

    private fun initView() {
        edtCollabDescription = findViewById(R.id.edtCollabDescription)
        edtCollabName = findViewById(R.id.edtCollabName)
        ivBack = findViewById(R.id.ivBack)
        tvSendReq = findViewById(R.id.tvSendReq)
        followersRecycView = findViewById(R.id.followersRecycView)
        tvSendReq?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)
        selectedFollowersAdapter = SelectedFollowersAdapter(followersList!!, this, this)
        followersRecycView?.layoutManager = GridLayoutManager(this, 2)
        followersRecycView?.adapter = selectedFollowersAdapter
    }

    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.tvSendReq -> {

                if (edtCollabName?.text?.toString()?.isEmpty()!!) {
                    Toast.makeText(this, "Please enter collab name", Toast.LENGTH_SHORT).show()
                    return
                }
                if (edtCollabDescription?.text?.toString()?.isEmpty()!!) {
                    Toast.makeText(this, "Please enter collab description", Toast.LENGTH_SHORT).show()
                    return
                }
                createCollabRoomApi()

            }
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

    private fun createCollabRoomApi() {
        /*{
            "collab_room": {
            "name": "second collab room",
            "collab_room_type": "jugalbandhi",
            "receiver_ids": [2],
            "message": "this is for jugalbandhi type"
        }
        }*/

        Common.showLoader(this)
        val json = JsonObject()
        val collab = JsonObject()
        val receiverIdArr = JsonArray()

        for (i in receiver_ids?.indices!!) {
            receiverIdArr.add(receiver_ids?.get(i))
        }
        collab.addProperty("name", edtCollabName?.text?.toString())
        collab.addProperty("message", edtCollabDescription?.text?.toString())
        collab.addProperty("collab_room_type", collabType)
        collab.add("receiver_ids", receiverIdArr)
        json.add("collab_room", collab)

        Log.e("TAG", "createCollabRoomApi: " + json)
        collabSessionviewModel.callApiCollabRoomCreate(prefManager?.getAccessToken()!!, json)
        collabSessionviewModel.getCollabRoomCreated()?.observe(this, {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("TAG", "createCollabRoomApi: " + it.message)
                    showDialogue(this, "Collab Request sent!",
                            "Hold tight, you have sent a collab request. you will be notified once your collab request is accepted. While you wait here's something for you to explore."
                    )
                }
            }
        })
    }

    override fun onPositionClick(variable: Int, check: Int) {
        if (check == 4) {
            Log.e("TAGG0", "onPositionClick: " + followersList)
            if (followersList?.size!! > 0) {
                followersList?.removeAt(variable)
            }
            Log.e("TAGG1", "onPositionClick: " + followersList)
            selectedFollowersAdapter?.notifyDataSetChanged()


        }
    }


    fun showDialogue(
            activity: Activity,
            title: String,
            message: String,
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
        btnNo?.text = "Explore More"
        btnYes?.text = "Join Collab"
        btnYes.setOnClickListener {
            builder.dismiss()
            finishAffinity()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("type", "join_collab")
            startActivity(intent)
        }

        btnNo?.setOnClickListener {
            builder?.dismiss()
            finishAffinity()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        ivClose.setOnClickListener {
            builder?.dismiss()
        }
        builder.setView(root)
        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

}