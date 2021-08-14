package com.tntra.pargo.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.tntra.pargo.R
import com.tntra.pargo.adapter.SelectedFollowersAdapter
import com.tntra.pargo.common.onClickAdapter
import org.json.JSONException
import org.json.JSONObject

class SendRequestActivity : AppCompatActivity(), View.OnClickListener, onClickAdapter {

    private var tvSendReq: TextView? = null
    var followers: String? = ""
    var jsonObject: JsonObject = JsonObject();
    private var followersRecycView: RecyclerView? = null
    private var selectedFollowersAdapter: SelectedFollowersAdapter? = null
    private var followersList: ArrayList<String>? = ArrayList()
    private var ivBack: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request_acrivity)

        try {
            if (intent != null) {
                followers = intent.getStringExtra("followers")
                Log.e("TAGG", "onCreate: " + followers)

                val obj = JSONObject(followers)
                val arr = obj.getJSONArray("followers")

                followersList?.clear()
                for (i in 0 until arr.length()) {
                    Log.e("TAGG", "onCreate: " + arr[i])
                    followersList?.add(arr[i].toString())
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        initView()
    }

    private fun initView() {
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
                val intent = Intent(this, RequestSentActivity::class.java)
                startActivity(intent)
            }
            R.id.ivBack -> {
                onBackPressed()
            }
        }
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

}