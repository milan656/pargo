package com.tntra.pargo.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tntra.pargo.R
import com.tntra.pargo.activities.CollabStudioActivity
import com.tntra.pargo.activities.GoLiveScreenActivity
import com.tntra.pargo.activities.VideoUploadActivity
import com.tntra.pargo.adapter.RequestListAdapter
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.onClickAdapter
import com.tntra.pargo.viewmodel.collab.CollabSessionviewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CollabFragment : Fragment(), onClickAdapter, View.OnClickListener {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var collabSessionviewModel: CollabSessionviewModel
    private var prefManager: PrefManager? = null
    private var requestAceptedRecycView: RecyclerView? = null
    private var requestListAdapter: RequestListAdapter? = null
    private var requestList: ArrayList<String>? = ArrayList()
    private var startCollabCard: CardView? = null

    private var cardUploadContent: CardView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collab, container, false)
        collabSessionviewModel = ViewModelProviders.of(this).get(CollabSessionviewModel::class.java)
        prefManager = context?.let { PrefManager(it) }
        initView(view)
        return view
    }

    private fun initView(view: View?) {
        cardUploadContent = view?.findViewById(R.id.cardUploadContent)
        requestAceptedRecycView = view?.findViewById(R.id.requestAceptedRecycView)
        startCollabCard = view?.findViewById(R.id.startCollabCard)
        startCollabCard?.setOnClickListener(this)
        cardUploadContent?.setOnClickListener(this)
        for (i in 1..10) {
            requestList?.add("")
        }
        requestListAdapter = activity?.let {
            RequestListAdapter(requestList!!, it, this)
        }
        requestAceptedRecycView?.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        requestAceptedRecycView?.adapter = requestListAdapter

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                CollabFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onPositionClick(variable: Int, check: Int) {
        val intent = Intent(context, GoLiveScreenActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.startCollabCard -> {
                val intent = Intent(context, CollabStudioActivity::class.java)
                startActivity(intent)
            }
            R.id.cardUploadContent -> {
//                val intent = Intent(context, VideoUploadActivity::class.java)
//                startActivity(intent)

                val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission(context as FragmentActivity?)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }

                if (result) {
                    showBottomSheetdialog(context)
                }

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(context: FragmentActivity?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (this.let {
                        context?.let { it1 ->
                            ContextCompat.checkSelfPermission(
                                    it1,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        }
                    } !== PackageManager.PERMISSION_GRANTED
                    || context?.let {
                        ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.CAMERA
                        )
                    } !== PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) !== PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_MEDIA_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.MANAGE_DOCUMENTS
                    ) !== PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.CAMERA
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.ACCESS_MEDIA_LOCATION
                        )
                        || ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.MANAGE_DOCUMENTS
                        )
                ) {
                    val alertBuilder = android.app.AlertDialog.Builder(context)
                    alertBuilder.setCancelable(false)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("Permission is necessary for choose file from device")
                    alertBuilder.setPositiveButton(
                            android.R.string.yes
                    ) { dialog, which ->
                        requestPermissions(
                                arrayOf<String>(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.MANAGE_DOCUMENTS,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_MEDIA_LOCATION
                                ),
                                123
                        )
                    }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {
                    requestPermissions(
                            arrayOf<String>(
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.MANAGE_DOCUMENTS,
                                    Manifest.permission.ACCESS_MEDIA_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE

                            ), 123
                    );

                }
                return false
            } else {
                return true
            }
        } else {
            return true
        }
    }

    private fun showBottomSheetdialog(
            context: Context?,

            ) {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.dialogue_pick_video, null)
        val imageDialog = context?.let { BottomSheetDialog(it, R.style.CustomBottomSheetDialogTheme) }

        imageDialog?.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        imageDialog?.window?.setLayout(width, height)
        imageDialog?.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        imageDialog?.setContentView(view)

        val cancelCard = view.findViewById<CardView>(R.id.cancelCard)
        val tvGallaryOption = view.findViewById<TextView>(R.id.tvGallaryOption)
        val tvRecordOption = view.findViewById<TextView>(R.id.tvRecordOption)

        tvRecordOption.text = "Record Audio / video"

        cancelCard?.setOnClickListener {
            imageDialog?.dismiss()
        }

        tvGallaryOption.setOnClickListener {
            imageDialog?.dismiss()
//            Log.e("TAGG", "showBottomSheetdialog: " + type)

            val intent = Intent(context, VideoUploadActivity::class.java)
            startActivity(intent)

        }

        tvRecordOption.setOnClickListener {
            imageDialog?.dismiss()

        }

        imageDialog?.show()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            123 -> {
                if (grantResults[1] != -1) {
                    if (grantResults.size > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        showBottomSheetdialog(context)
                    }
                }
            }


        }
    }

}