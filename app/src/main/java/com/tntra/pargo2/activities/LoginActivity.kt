package com.tntra.pargo2.activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import androidx.lifecycle.viewModelScope;
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.Common.Companion.showDialogue
import com.tntra.pargo2.common.PrefManager
import com.tntra.pargo2.viewmodel.LoginActivityViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.gson.JsonObject
import com.tntra.pargo2.R
import com.tntra.pargo2.activities.MainActivity
import com.tntra.pargo2.model.login_response.UserLoginModel

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var loginViewModel: LoginActivityViewModel
    private lateinit var prefManager: PrefManager
    private lateinit var edtLoginEmail: EditText
    private lateinit var edtLoginPassword: EditText

    private lateinit var btnLoginToDashBoard: Button
    private var userLoginModel: UserLoginModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()

        smsPermission()

        try {
            Common.isCalling = false
        } catch (e: Exception) {
            e.printStackTrace()
        }

        loginViewModel = ViewModelProviders.of(this).get(LoginActivityViewModel::class.java)

    }

    private fun init() {
        prefManager = PrefManager(this@LoginActivity)

        edtLoginEmail = findViewById<EditText>(R.id.edtLoginEmail)
        edtLoginPassword = findViewById<EditText>(R.id.edtLoginPassword)

        btnLoginToDashBoard = findViewById(R.id.btnLoginToDashBoard)
        btnLoginToDashBoard.setOnClickListener(this)

    }

    private fun smsPermission() {
        val pERMISSIONS = arrayOf(
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS
        )
        if (!hasPermissions(this, *pERMISSIONS)) {
            ActivityCompat.requestPermissions(this, pERMISSIONS, 1)
        }
    }

    fun hasPermissions(context: Context?, vararg permissions: String?): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                                context,
                                permission!!
                        ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLoginToDashBoard -> userLogin()
        }
    }

    private fun userLogin() {
        edtLoginEmail.requestFocus()
        if (edtLoginEmail.text?.trim()?.length == 0) {
            edtLoginEmail.error = "Please Enter Email Address"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }
        if (!edtLoginEmail.text?.toString().equals("")) {
            if (!Common.emailRegex.matcher(edtLoginEmail.text.toString())
                            .matches()
            ) {
                edtLoginEmail.error = "Please Enter Valid Email Address"
                return
            }
        }

        if (edtLoginPassword.text?.trim()?.length == 0) {
            edtLoginPassword.error = "Please Enter Password"
            //Common.showShortToast("Please enter userid", this@LoginActivity)
            return
        }

        startSMSListener()

        Common.showLoader(this@LoginActivity)

        val jsonObject = JsonObject()

        val userObj = JsonObject()
        userObj.addProperty("email", edtLoginEmail.text?.toString())
        userObj.addProperty("password", edtLoginPassword.text.toString())
        jsonObject.add("user", userObj)

        Log.e("getobject", "" + jsonObject)
        loginViewModel.initTwo(jsonObject)
        loginViewModel.getUserInfo()?.observe(this, {
            Common.hideLoader()
            if (it != null) {
                if (it.success) {
                    Log.e("TAG", "userLogin: " + it.authorization)
                    prefManager.setAccessToken(it.authorization)
                    prefManager.setUserId(it.user.id.toInt())
                    prefManager.setUserName(it.user.attributes.profile.name)
                    prefManager.isLogin(true)
                    if (it.user.attributes.roles[0].name.equals("Creator")) {
                        prefManager.isCreator(true)
                    } else {
                        prefManager.isCreator(false)
                    }
                    userLoginModel = it
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("name", userLoginModel?.user?.attributes?.roles!![0].name)
                    startActivity(intent)
//                    showDialogue(this, "Success", "" + it, false, true)

                } else {
                    Common.hideLoader()
//                    if (it.error != null && it.error.get(0).message != null) {
//                    }
                    showDialogue(this, "Oops!", it.message, false, false)
                }
            }
        })
    }

    private fun startSMSListener() {
        try {
            val client = SmsRetriever.getClient(this)
            val task = client.startSmsRetriever()
            task.addOnSuccessListener {
                // API successfully started
            }
            task.addOnFailureListener {
                // Fail to start API
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
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
        ivClose?.visibility = View.INVISIBLE
        btnYes.setOnClickListener {
            builder.dismiss()
            if (isBackPressed) {
                activity.finish()
            }
            if (isnavigate) {
                val intent = Intent(activity, HomeActivity::class.java)
                intent.putExtra("name", userLoginModel?.user?.attributes?.roles!![0].name)
                activity.startActivity(intent)
            }

        }

        ivClose.setOnClickListener {
            builder?.dismiss()
        }
        builder.setView(root)

        builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        builder.show()
    }

}