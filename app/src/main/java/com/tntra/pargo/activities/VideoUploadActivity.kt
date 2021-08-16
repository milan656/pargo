package com.tntra.pargo.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.amazonaws.ClientConfiguration
import com.amazonaws.Protocol
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tntra.pargo.R
import com.tntra.pargo.common.Common
import com.tntra.pargo.common.Common.Companion.getFile
import com.tntra.pargo.common.PrefManager
import com.tntra.pargo.common.RetrofitCommonClass
import com.tntra.pargo.model.contentcreate.ContentCreateModel
import com.tntra.pargo.model.generes.Genre
import com.tntra.pargo.networkApi.login.ContentApi
import com.tntra.pargo.viewmodel.ContentViewModel
import com.tntra.pargo.viewmodel.generes.GeneresViewModel
import okhttp3.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList


class VideoUploadActivity : AppCompatActivity(), View.OnClickListener {

    private var ivCoverpicView: ImageView? = null
    private var prefManager: PrefManager? = null

    private var imageUpload = false
    private var fileUri_: Uri? = null
    private var imagefileUri_: Uri? = null
    private var post_type: String = ""
    private val PERMISSION_CODE = 1010;
    private val IMAGE_CAPTURE_CODE = 1011
    var image_uri: Uri? = null
    var geners_id: String = ""
    var exploreGenList: ArrayList<Genre>? = ArrayList()

    private lateinit var generesViewModel: GeneresViewModel

    private var contentViewModel: ContentViewModel? = null

    private var accessKey: String = "AKIAYI7XP4UNI7KFDUFZ"
    private var secreteKey: String = "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"
    private var bucket_name: String = "pargo-back-end-devlopment"

    val PICK_IMAGE_REQUEST = 100
    private var videoView: CardView? = null
    private var cardImage: CardView? = null

    private var selectedVideoUrl: String? = ""
    private var selectedImageUrl: Boolean? = false

    private var chkfollowing: CheckBox? = null
    private var chkfollower: CheckBox? = null
    private var chkEveryone: CheckBox? = null
    private var tvSelectfile: TextView? = null
    private var tvUpload: TextView? = null
    private var tvTitle: TextView? = null
    private var edtTitleVideo: EditText? = null
    private var edtDesc: EditText? = null
    private var ivVidoFile: ImageView? = null
    private var ivVidoIcon: ImageView? = null
    private var tvFileTime: TextView? = null
    private var ivBack: ImageView? = null
    private var spinnerTags: Spinner? = null
    private var listofTags: ArrayList<String>? = ArrayList()

    private var videoTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_upload)
        prefManager = PrefManager(this)
        generesViewModel = ViewModelProviders.of(this).get(GeneresViewModel::class.java)
        contentViewModel = ViewModelProviders.of(this).get(ContentViewModel::class.java)
        initView()
    }

    private fun initView() {
        ivBack = findViewById(R.id.ivBack)
        spinnerTags = findViewById(R.id.spinnerTags)
        ivVidoFile = findViewById(R.id.ivVidoFile)
        ivVidoIcon = findViewById(R.id.ivVidoIcon)
        tvFileTime = findViewById(R.id.tvFileTime)
        videoView = findViewById(R.id.videoView)
        tvSelectfile = findViewById(R.id.tvSelectfile)
        tvUpload = findViewById(R.id.tvUpload)
        tvTitle = findViewById(R.id.tvTitle)
        edtTitleVideo = findViewById(R.id.edtTitleVideo)
        edtDesc = findViewById(R.id.edtDesc)
        tvUpload?.visibility = View.VISIBLE
        cardImage = findViewById(R.id.cardImage)
        ivCoverpicView = findViewById(R.id.ivCover)

        chkEveryone = findViewById(R.id.chkEveryone)
        chkfollower = findViewById(R.id.chkfollower)
        chkfollowing = findViewById(R.id.chkfollowing)

        videoView?.setOnClickListener(this)
        cardImage?.setOnClickListener(this)
        tvUpload?.setOnClickListener(this)
        ivBack?.setOnClickListener(this)

        tvTitle?.text = "Video"
        setAdapter()

        selectAccessbility()
        videoView?.performClick()
        getGeneresList()

    }

    private fun getGeneresList() {

        this.let { generesViewModel.callApiGeneresList(prefManager?.getAccessToken()!!, it) }

        generesViewModel.getGeneresList()?.observe(this, {

            if (it != null) {
                if (it.success) {
                    exploreGenList?.clear()
                    exploreGenList?.addAll(it.genres)

                    listofTags?.clear()
                    for (i in exploreGenList?.indices!!) {
                        listofTags?.add(exploreGenList?.get(i)?.attributes?.name!!)
                    }

                    setAdapter()

                }
            }
        })


    }


    private fun selectAccessbility() {

        chkEveryone?.setOnClickListener {

            chkEveryone?.isChecked = true

            chkfollowing?.isChecked = false
            chkfollower?.isChecked = false
        }
        chkfollowing?.setOnClickListener {

            chkfollowing?.isChecked = true

            chkEveryone?.isChecked = false
            chkfollower?.isChecked = false
        }
        chkfollower?.setOnClickListener {

            chkfollower?.isChecked = true

            chkfollowing?.isChecked = false
            chkEveryone?.isChecked = false
        }
    }

    private fun setAdapter() {

        val arrayAdapterYear =
                ArrayAdapter(
                        this@VideoUploadActivity,
                        android.R.layout.simple_spinner_item,
                        listofTags!!
                )
        arrayAdapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTags?.adapter = arrayAdapterYear

        spinnerTags?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                Log.e("TAGG", "onItemSelected: " + listofTags?.get(position))
                Log.e("TAGG", "onItemSelected: " + exploreGenList?.get(position)?.attributes?.name)
                Log.e("TAGG", "onItemSelected: " + exploreGenList?.get(position)?.id)
                geners_id = exploreGenList?.get(position)?.id!!
            }
        }
    }


    override fun onClick(v: View?) {
        val id = v?.id
        when (id) {
            R.id.videoView -> {

                imageUpload = false
//                showBottomSheetdialog(this, "video")
                pickVideoFromGallary("video")
            }
            R.id.cardImage -> {
                imageUpload = true
                showBottomSheetdialog(this, "image")
            }
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.tvUpload -> {
                if (selectedVideoUrl.equals("")) {
                    Toast.makeText(this, "Please Upload video / audio file", Toast.LENGTH_SHORT).show()
                    return
                }
                if (edtTitleVideo?.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                    return
                }
                if (edtDesc?.text.toString().isEmpty()) {
                    Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show()
                    return
                }
                if (spinnerTags?.selectedItem.toString().isEmpty()) {
                    Toast.makeText(this, "Please select tag", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!selectedImageUrl!!) {
                    Toast.makeText(this, "Please select cover photo", Toast.LENGTH_SHORT).show()
                    return
                }
                if (!chkEveryone?.isChecked!! && !chkfollower?.isChecked!! && !chkfollowing?.isChecked!!) {
                    Toast.makeText(this, "Please select audience", Toast.LENGTH_SHORT).show()
                    return
                }

//                uploadContentApi

                var imagePath: File? = null
                if (imagefileUri_ != null) {
                    imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        getFile(this@VideoUploadActivity, imagefileUri_)
                    } else {
                        TODO("VERSION.SDK_INT < KITKAT")
                    }
                }


                var accessbility: String = ""
                if (chkEveryone?.isChecked!!) {
                    accessbility = "everyone"
                }
                if (chkfollowing?.isChecked!!) {
                    accessbility = "followings"
                }
                if (chkfollower?.isChecked!!) {
                    accessbility = "followers"
                }

                Log.e("TAGG", "onClick: " + selectedImageUrl + " " + selectedVideoUrl + " " + edtTitleVideo?.text.toString() + " " +
                        edtDesc?.text.toString() + " " + geners_id + " " + accessbility + " " +
                        imagePath?.path + " " + " " + " " + tvFileTime?.text?.toString())

//                Common.showDialogue(this, "Success", "Content Uploaded Successfully", true, false)
//                var file: File = File("")
                callApiUploadContent(imagePath!!, imagePath.inputStream(), accessbility)
            }
        }
    }

    private fun callApiUploadContent(imagePath: File, inputStream: InputStream, accessbility: String) {

        Common.showLoader(this)
        val part = MultipartBody.Part.createFormData(
                "content[cover_img]", imagePath.name, RequestBody.create(
                MediaType.parse("image/*"),
                inputStream.readBytes()
        )
        )

        val title: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                edtTitleVideo?.getText()
                        .toString())
        val desc: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                edtDesc?.getText()
                        .toString())
        val geners: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                geners_id)
        val accessbili: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                accessbility)
        val type: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                post_type)
        val post: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                selectedVideoUrl!!)
        val fileTime: RequestBody = RequestBody.create(MediaType.parse("text/plain"),
                tvFileTime?.text?.toString()!!)

        val warrantyApi = RetrofitCommonClass.createService(ContentApi::class.java)
        var call: Call<ContentCreateModel>? = null

        call = warrantyApi.uploadContent(prefManager?.getAccessToken()!!, part, title, desc, geners, accessbili, type, post, fileTime)

        call?.enqueue(object : Callback<ContentCreateModel> {
            override fun onResponse(call: Call<ContentCreateModel>?, response: Response<ContentCreateModel>?) {

                Common.hideLoader()
                if (response?.isSuccessful!!) {
                    Log.e("TAGG", "onResponse: " + response.body())
                    Common.showDialogue(this@VideoUploadActivity, "Success", "Content Uploaded Successfully", true, false)
                } else {

                    val responce = response.errorBody()?.string()
                    val jsonObjectError = JSONObject(responce)

                    val contentCreateModel: ContentCreateModel =
                            Common.getErrorModel(jsonObjectError, "ContentCreateModel") as ContentCreateModel
                    Log.e("TAGG", "onResponse: " + contentCreateModel.message)
                }
            }

            override fun onFailure(call: Call<ContentCreateModel>?, t: Throwable?) {
                Log.e("TAGG", "onFailure: " + t?.cause + " " + t?.message)
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun checkPermission(context: FragmentActivity?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (this.let {
                        ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    } !== PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
                    ) !== PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) !== PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                            this,
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
                                Manifest.permission.MANAGE_DOCUMENTS
                        )
                ) {
                    val alertBuilder = android.app.AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(
                            android.R.string.yes
                    ) { dialog, which ->
                        requestPermissions(
                                arrayOf<String>(
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.MANAGE_DOCUMENTS,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {

                    val selectedImage = data?.data
                    fileUri_ = selectedImage
                    var imagePath: File? = null

                    if (selectedImage != null) {
                        imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            getFile(this@VideoUploadActivity, selectedImage)
                        } else {
                            TODO("VERSION.SDK_INT < KITKAT")
                        }
                        Log.i("imagePath0", "++++" + imagePath?.name)
                    }

                    if (imagePath?.name == null) {
                        val path = data?.data?.path

                        val uri = Uri.fromFile(File(path!!))
                        imagePath = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            getFile(this@VideoUploadActivity, uri)
                        } else {
                            TODO("VERSION.SDK_INT < KITKAT")
                        }
                        Log.i("imagePath1", "++++" + imagePath?.name)
                    }
                    if (imageUpload) {
                        try {
                            val bmThumbnail: Bitmap

                            bmThumbnail = ThumbnailUtils.createImageThumbnail(imagePath?.path!!, MediaStore.Video.Thumbnails.MINI_KIND)!!

                            ivCoverpicView?.setImageBitmap(bmThumbnail)
                            selectedImageUrl = true
                            imagefileUri_ = selectedImage

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        try {
                            val extension: String = Common.getfileExtension(this, selectedImage!!)
                            if (!extension.equals("")) {
                                if (extension.equals("mp4") || extension.equals("mov")) {
                                    post_type = "video"
                                } else {
                                    post_type = "audio"
                                }
                            }
                            tvSelectfile?.visibility = View.GONE
//                            Log.e("zvzvzv", "onStateChanged: " + "https://$bucket_name.s3-ap-south-1.amazonaws.com/Android/$filename")
                            selectedVideoUrl = "https://$bucket_name.s3-ap-south-1.amazonaws.com/Android/${imagePath?.name}"
                            val bmThumbnail: Bitmap

                            if (post_type.equals("video")) {
                                bmThumbnail = ThumbnailUtils.createVideoThumbnail(imagePath?.path!!, MediaStore.Video.Thumbnails.MINI_KIND)!!
                            } else {
                                bmThumbnail = ThumbnailUtils.createAudioThumbnail(imagePath?.path!!, MediaStore.Video.Thumbnails.MINI_KIND)!!
                            }

                            ivVidoFile?.setImageBitmap(bmThumbnail)
                            ivCoverpicView?.setImageBitmap(bmThumbnail)
                            ivVidoIcon?.visibility = View.VISIBLE

                            imagefileUri_ = Common.getImageUriFromBitmap(this, bmThumbnail)
                            if (imagefileUri_ != null) {
                                selectedImageUrl = true
                            }
                            val retriever = MediaMetadataRetriever()
                            retriever.setDataSource(this, Uri.fromFile(imagePath))
                            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                            val millis: Long = time.toLong()
                            val minutes: Long = millis / 1000 / 60
                            val seconds = (millis / 1000 % 60)
                            retriever.release()

                            tvFileTime?.text = "" + minutes + ":" + seconds
                            tvFileTime?.visibility = View.VISIBLE

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        upload_FileAWS_S3_Bucket(imagePath?.name!!, imagePath)
                    }

                } else {
                    if (imageUpload) {
                        selectedImageUrl = false
                    }
                }
            }
            IMAGE_CAPTURE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {

                    imagefileUri_ = image_uri

                    if (imageUpload) {
                        ivCoverpicView?.setImageURI(image_uri)
                        selectedImageUrl = true
                    }
                } else {
                    if (imageUpload) {
                        selectedImageUrl = false
                    }
                }

            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Common.hideLoader()
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
            123 -> {
                if (grantResults[1] != -1) {
                    if (grantResults.size > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    ) {
                        try {
                            val intent = Intent(Intent.ACTION_GET_CONTENT)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                            }

                            if (Build.VERSION.SDK_INT < 19) {
                                if (imageUpload) {
                                    intent.type = "image/*"
                                } else {
                                    intent.type = "video/* audio/*"
                                }
                            } else {
                                intent.addCategory(Intent.CATEGORY_OPENABLE)
                                if (imageUpload) {
                                    intent.type = "image/*"
                                    intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                                } else {
                                    intent.type = "video/* audio/*"
                                    intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/*", "audio/*"))
                                }
                            }
                            intent.action = Intent.ACTION_GET_CONTENT
                            startActivityForResult(intent, PICK_IMAGE_REQUEST)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }


        }
    }

    private fun upload_FileAWS_S3_Bucket(filename: String, file: File) {
        Common.showLoader(this)
        val policy: StrictMode.ThreadPolicy =
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy)

        val configuration = ClientConfiguration()
        configuration.maxErrorRetry = 3
        configuration.connectionTimeout = 801000
        configuration.socketTimeout = 801000
//        configuration.protocol = Protocol.HTTP
        val credentials = BasicAWSCredentials(
                accessKey,
                secreteKey);
        val s3 = AmazonS3Client(credentials, configuration)
//        s3.timeOffset = 10000000
        java.security.Security.setProperty("networkaddress.cache.ttl", "60");
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        s3.setEndpoint("https://s3-ap-south-1.amazonaws.com/");
//        val request = GeneratePresignedUrlRequest(bucket_name, objectName)
//        val objectURL: URL = s3.generatePresignedUrl(request)
        val transferUtility = TransferUtility.builder()
                .defaultBucket(
                        bucket_name)
                .context(this.applicationContext)
                .s3Client(s3).build();

        val uploadObserver =
                transferUtility.upload(
                        bucket_name,
                        "Android/" + filename,
                        file,
                        CannedAccessControlList.PublicRead
                )

        uploadObserver.setTransferListener(object : TransferListener {
            override fun onStateChanged(id: Int, state: TransferState) {
                if (state == TransferState.COMPLETED) {

                    Log.e("zvzvzv", "onStateChanged: " + "https://$bucket_name.s3-ap-south-1.amazonaws.com/Android/$filename")
//                    https://pargo-back-end-devlopment.s3-ap-south-1.amazonaws.com/Android/VID_20210724_062618.mp4
//https://pargo-back-end-devlopment.s3-ap-south-1.amazonaws.com/Android/VID_20210811_142825.mp4
//                    https://pargo-back-end-devlopment.s3-ap-south-1.amazonaws.com/Android/maxkomusic-digital-world.mp3
                    if (!imageUpload) {
                        selectedVideoUrl = "https://$bucket_name.s3-ap-south-1.amazonaws.com/Android/$filename"
                    } else {
                        selectedVideoUrl = ""
                    }

                    Toast.makeText(this@VideoUploadActivity, "File Upload Successfully", Toast.LENGTH_SHORT).show()
                } /*else if (TransferState.FAILED == state) {
                    Toast.makeText(this@VideoUploadActivity, "File Upload Failed", Toast.LENGTH_SHORT).show()
//                    failed
                }*/
            }

            override fun onProgressChanged(id: Int, current: Long, total: Long) {
                val done = (((current.toDouble() / total) * 100.0).toInt())
                Log.d("zvzvzv", "UPLOAD - - ID: $id, percent done = $done")
                if (done == 100) {
                    Common.hideLoader()
                }
            }

            override fun onError(id: Int, ex: Exception) {
                Common.hideLoader()
                selectedVideoUrl = ""
                Toast.makeText(this@VideoUploadActivity, "UPLOAD ERROR: " + ex.message.toString(), Toast.LENGTH_SHORT).show()
                Log.d("zvzvzv", "UPLOAD ERROR - - ID: $id - - EX: ${ex.message.toString()}")
                Log.d("zvzvzv", "UPLOAD ERROR - - ID: $id - - EX: ${ex.cause}")
            }
        })
    }


    private fun showBottomSheetdialog(
            context: Context?,
            type: String,
    ) {
        val view = LayoutInflater.from(context)
                .inflate(R.layout.dialogue_pick_video, null)
        val imageDialog = BottomSheetDialog(this, R.style.CustomBottomSheetDialogTheme)

        imageDialog.setCancelable(false)
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        imageDialog.window?.setLayout(width, height)
        imageDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);
        imageDialog.setContentView(view)

        val cancelCard = view.findViewById<CardView>(R.id.cancelCard)
        val tvGallaryOption = view.findViewById<TextView>(R.id.tvGallaryOption)
        val tvRecordOption = view.findViewById<TextView>(R.id.tvRecordOption)

        if (imageUpload) {
            tvRecordOption.text = "Choose From Camera"
        } else {
            tvRecordOption.text = "Record Audio / video"
        }
        cancelCard?.setOnClickListener {
            imageDialog.dismiss()
        }

        tvGallaryOption.setOnClickListener {
            imageDialog.dismiss()
            Log.e("TAGG", "showBottomSheetdialog: " + type)
            if (type.equals("image")) {
                pickFileFromGallary(type)
            } else {
                pickVideoFromGallary(type)
            }
        }

        tvRecordOption.setOnClickListener {
            imageDialog.dismiss()
            if (imageUpload) {
                cameraPermission()
            }
        }

        imageDialog.show()
    }

    fun pickFileFromGallary(type: String) {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(this)
        } else {

            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                }

                if (Build.VERSION.SDK_INT < 19) {
                    if (imageUpload) {
                        intent.type = "image/*"
                    } else {
                        intent.type = "video/* audio/*"
                    }
                } else {
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    if (imageUpload) {
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                    } else {
                        intent.type = "video/* audio/*"
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/*", "audio/*"))
                    }
                }
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (result == true) {
            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                }

                if (Build.VERSION.SDK_INT < 19) {
                    if (imageUpload) {
                        intent.type = "image/*"
                    } else {
                        intent.type = "video/* audio/*"
                    }
                } else {
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    if (imageUpload) {
                        intent.type = "image/*"
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*"))
                    } else {
                        intent.type = "video/* audio/*"
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/*", "audio/*"))
                    }
                }
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun pickVideoFromGallary(type: String) {
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission(this)
        } else {
            try {
                Log.e("TAGG", "pickFileFromGallary: " + type)
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.setType("video/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                }
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, PICK_IMAGE_REQUEST)

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        if (result == true) {
            try {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                Log.e("TAGG", "pickFileFromGallary: video" + type)
                intent.setType("video/*");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
                }
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(intent, PICK_IMAGE_REQUEST)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun cameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.MANAGE_DOCUMENTS)
                    == PackageManager.PERMISSION_DENIED
            ) {
                //permission was not enabled
                val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_DOCUMENTS
                )
                //show popup to request permission
                requestPermissions(permission, PERMISSION_CODE)
            } else {
                //permission already granted
                openCamera()
            }
        } else {
            //system os is < marshmallow
            openCamera()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    private fun File.writeBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int) {
        outputStream().use { out ->
            bitmap.compress(format, quality, out)
            out.flush()
        }
    }
}