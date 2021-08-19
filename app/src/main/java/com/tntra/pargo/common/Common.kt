package com.tntra.pargo.common

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Resources
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.MediaMetadataRetriever
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.core.widget.ImageViewCompat
import androidx.exifinterface.media.ExifInterface
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.util.IOUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo.R
import com.tntra.pargo.activity.LoginActivity
import com.tntra.pargo.model.collabroom.CollabRoomCreateModel
import com.tntra.pargo.model.comments.list.CommentsListingModel
import com.tntra.pargo.model.content_list.ContentListModel
import com.tntra.pargo.model.followers.FollowersListModel
import com.tntra.pargo.model.login.OtpModel
import com.tntra.pargo.model.login_response.UserLoginModel
import com.tntra.pargo.model.notification.NotificationListModel
import com.tntra.pargo.model.treading_content.TreadingContentModel
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.io.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


class Common {

    companion object {
        //        var url: String? = "https://staging-backend.aapkedoorstep.com/api/"
//        var url: String? = "https://207456b0c6ec.ngrok.io/"
        var url: String? = "http://65.0.11.205/"

        var isCalling: Boolean? = false
        val uid_key = "uid_key"

        var emailRegex = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        )

        private var dialogue: Dialog? = null

        @SuppressLint("SimpleDateFormat")
        fun timeStampaTimeago(time1: String): String? {
            var time: String = time1
            try {

                val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                format.timeZone = TimeZone.getTimeZone("UTC")
                val past = format.parse(time)
                val now = Date()
                val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past?.time!!)
                val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
                val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
                val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
                val seconds = Math.abs(seconds1)
                val minutes = Math.abs(minutes1)
                val hours = Math.abs(hours1)
                val days = Math.abs(days1)
                if (seconds < 60) {
                    println("$seconds seconds ago")
                    time = "$seconds seconds ago"
                } else if (minutes < 60) {
                    println("$minutes minutes ago")
                    time = "$minutes minutes ago"
                } else if (hours < 24) {
                    println("$hours hours ago")
                    time = "$hours hours ago"
                } else if (days < 30) {

                    if ("$days".equals("1")) {
                        time = "$days day ago"
                    } else {
                        time = "$days days ago"
                    }
                } else if (days < 365) {
                    val month = days / 30
                    time = "$month month ago"
                } else if (days >= 365) {
                    time = (days / 365).toString() + " year ago"
                } else {
                    println("$days days ago")
                    time = "$days days ago"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return time
        }

        fun dateFotmatInDate(date: String): Date {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            val returnDate = formatter.parse(date)
            return returnDate
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun findDifference(
                start_date: String?,
                end_date: String?,
        ): String {
            var difference_In_Time = 0L
            var difference_In_Seconds = 0L
            var difference_In_Minutes = 0L
            var difference_In_Hours = 0L
            var difference_In_Years = 0L
            var difference_In_Days = 0L
            // SimpleDateFormat converts the
            // string format to date object
            val sdf = SimpleDateFormat(
                    "dd-MM-yyyy hh:mm:ss"
            )

            // Try Block
            try {

                // parse method is used to parse
                // the text from a string to
                // produce the date
                val d1 = sdf.parse(start_date)
                val d2 = sdf.parse(end_date)

                // Calucalte time difference
                // in milliseconds
                difference_In_Time = d2?.time!! - d1?.time!!

                // Calucalte time difference in
                // seconds, minutes, hours, years,
                // and days
                difference_In_Seconds = ((difference_In_Time
                        / 1000)
                        % 60)
                difference_In_Minutes = ((difference_In_Time
                        / (1000 * 60))
                        % 60)
                difference_In_Hours = ((difference_In_Time
                        / (1000 * 60 * 60))
                        % 24)
                difference_In_Years = (difference_In_Time
                        / (1000L * 60 * 60 * 24 * 365))
                difference_In_Days = ((difference_In_Time
                        / (1000 * 60 * 60 * 24))
                        % 365)

                // Print the date difference in
                // years, in days, in hours, in
                // minutes, and in seconds
                print(
                        "Difference "
                                + "between two dates is: "
                )
                println(
                        difference_In_Years
                                .toString() + ", "
                                + difference_In_Days
                                + ", "
                                + difference_In_Hours
                                + ", "
                                + difference_In_Minutes
                                + ", "
                                + difference_In_Seconds
                                + ""
                )


            } // Catch the Exception
            catch (e: ParseException) {
                e.printStackTrace()
            }
            val str = difference_In_Years
                    .toString() + "," +
                    difference_In_Days.toString() + "," + difference_In_Hours + "," + difference_In_Minutes + "," + difference_In_Seconds + ""

            return str
        }

        fun dateDifference(dateInPref: String): Long {

            val dateAccessToken = dateFotmatInDate(dateInPref)
            val date = Date()

            val diff = dateAccessToken.time - date.time
            val seconds = diff / 1000

            val minutes = seconds / 60
            val hours = minutes / 60

            Log.i("hours_", "++++" + hours + " " + minutes + " " + seconds)
            val days = hours / 24
            return days
        }

        fun getDeviceName(): String {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else capitalize(manufacturer) + " " + model
        }

        private fun capitalize(str: String): String {
            if (TextUtils.isEmpty(str)) {
                return str
            }
            val arr = str.toCharArray()
            var capitalizeNext = true

            val phrase = StringBuilder()
            for (c in arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(Character.toUpperCase(c))
                    capitalizeNext = false
                    continue
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true
                }
                phrase.append(c)
            }

            return phrase.toString()
        }

        fun getStringBuilder(str: String): StringBuilder {
            val strBuilder: java.lang.StringBuilder = java.lang.StringBuilder()

            val temp: Array<String> = str.split(",").toTypedArray()

            for (i in temp.indices) {
                strBuilder.append(temp.get(i)).append(",")
            }
            return strBuilder
        }

        fun saveImage(context: Context, bitmap: Bitmap, name: String, extension: String) {
            val folder =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(folder, name + extension)
            try {
                var stream: OutputStream? = null
                stream = FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.flush()
                stream.close()

                val fstream: FileOutputStream = FileOutputStream(file)
                fstream.write(file.readBytes())
                fstream.close()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }


        fun getFile(filename: String?): File {
            val folder =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            var myFile = File(folder, filename)
            val fstream = FileInputStream(myFile)
            val sbuffer = StringBuffer()
            var i: Int
            while (fstream.read().also { i = it } != -1) {
                sbuffer.append(i.toChar())
            }
            fstream.close()
            return myFile
        }

        fun dateDiffrence(past: Date?, now: Date?): String? {

            var time: String? = null
            val now = Date()
            val seconds1 = TimeUnit.MILLISECONDS.toSeconds(now.time - past?.time!!)
            val minutes1 = TimeUnit.MILLISECONDS.toMinutes(now.time - past?.time!!)
            val hours1 = TimeUnit.MILLISECONDS.toHours(now.time - past?.time!!)
            val days1 = TimeUnit.MILLISECONDS.toDays(now.time - past?.time!!)
            var seconds = Math.abs(seconds1)
            var minutes = Math.abs(minutes1)
            var hours = Math.abs(hours1)
            var days = Math.abs(days1)
            if (seconds < 60) {
                println("$seconds seconds")
                time = "$seconds seconds"
            } else if (minutes < 60) {
                println("$minutes minutes")
                time = "$minutes minutes"
            } else if (hours < 24) {
                println("$hours hours")
                time = "$hours hours"
            } else if (days < 30) {

                if ("$days".equals("1")) {
                    time = "$days day"
                } else {
                    time = "$days days"
                }
            } else if (days < 365) {
                var month = days / 30
                time = "$month month"
            } else if (days >= 365) {
                time = (days / 365).toString() + " year"
            } else {
                println("$days days")
                time = "$days days"
            }
            return time
        }

        fun ImageView.setTint(context: Context, @ColorRes colorId: Int) {
            val color = ContextCompat.getColor(context, colorId)
            val colorStateList = ColorStateList.valueOf(color)
            ImageViewCompat.setImageTintList(this, colorStateList)
        }

        fun ImageView.setTintNull() {
            ImageViewCompat.setImageTintList(this, null)
        }

        fun getErrorModel(jsonObject: JSONObject, modelName: String?): Any? {
            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            try {
                when (modelName) {

                    "UserModel" -> {
                        val userModel =
                                gson.fromJson(jsonObject.toString(), UserModel::class.java)
                        return userModel
                    }
                    "OtpModel" -> {
                        val OtpModel =
                                gson.fromJson(jsonObject.toString(), OtpModel::class.java)
                        return OtpModel
                    }
                    "UserLoginModel" -> {
                        val UserLoginModel =
                                gson.fromJson(jsonObject.toString(), UserLoginModel::class.java)
                        return UserLoginModel
                    }
                    "ContentListModel" -> {
                        val ContentListModel =
                                gson.fromJson(jsonObject.toString(), ContentListModel::class.java)
                        return ContentListModel
                    }

                    "FollowerModel" -> {
                        val FollowerModel =
                                gson.fromJson(jsonObject.toString(), FollowersListModel::class.java)
                        return FollowerModel
                    }

                    "TreadingContentModel" -> {
                        val TreadingContentModel =
                                gson.fromJson(jsonObject.toString(), TreadingContentModel::class.java)
                        return TreadingContentModel
                    }

                    "CommentsListingModel" -> {
                        val CommentsListingModel =
                                gson.fromJson(jsonObject.toString(), CommentsListingModel::class.java)
                        return CommentsListingModel
                    }

                    "FollowersListModel" -> {
                        val FollowersListModel =
                                gson.fromJson(jsonObject.toString(), FollowersListModel::class.java)
                        return FollowersListModel
                    }
                    "CollabRoomCreateModel" -> {
                        val CollabRoomCreateModel =
                                gson.fromJson(jsonObject.toString(), CollabRoomCreateModel::class.java)
                        return CollabRoomCreateModel
                    }
                    "NotificationListModel" -> {
                        val NotificationListModel =
                                gson.fromJson(jsonObject.toString(), NotificationListModel::class.java)
                        return NotificationListModel
                    }


                    else -> {
                        return null
                    }

                }
            } catch (e: java.lang.Exception) {

                e.printStackTrace()
                return null
            }
        }

        fun getModelreturn(
                modelName: String?,
                response: Response<ResponseBody>?,
                i: Int,
                context: Context?,
        ): Any? {

            //  val context = MainApplication.applicationContext()

            val gson: Gson
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()

            return try {

                var resp: String? = null

                if (i == 0) {
                    resp = response?.body()?.string()
                } else {
                    resp = response?.errorBody()?.string()
                }

                val jsonObject = JSONObject(resp)

                if (jsonObject.has("success")) {
                    if (jsonObject.getBoolean("success")) {

                        if (jsonObject.has("warningOrUpdate")) {
                            val jsonObjectForce = jsonObject.getJSONObject("warningOrUpdate")
                            showDialogueForWarning(context!!, jsonObjectForce)
                        }

                        return when (modelName) {

                            "UserModel" -> {
                                val userModel =
                                        gson.fromJson(jsonObject.toString(), UserModel::class.java)
                                return userModel
                            }
                            "OtpModel" -> {
                                val OtpModel =
                                        gson.fromJson(jsonObject.toString(), OtpModel::class.java)
                                return OtpModel
                            }
                            "UserLoginModel" -> {
                                val UserLoginModel =
                                        gson.fromJson(jsonObject.toString(), UserLoginModel::class.java)
                                return UserLoginModel
                            }

                            else -> {
                                return null
                            }

                        }
                    } else {

                        Log.e("getcalling", "" + isCalling)
                        try {

                            if (!isCalling!!) {

                                if (!jsonObject.has("error")) {
                                    return null
                                }
                                isCalling = true
                                val jsonArray: JSONArray = jsonObject.getJSONArray("error")
                                val jsonObjectError: JSONObject = jsonArray.getJSONObject(0)
                                if (jsonObjectError.has("statusCode") && jsonObjectError.getInt(
                                                "statusCode"
                                        ) == 401
                                ) {
                                    val prefManager = PrefManager(context!!)
                                    prefManager.clearAll()
                                    val intent = Intent(context, LoginActivity::class.java)
                                    intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    context.startActivity(intent)

                                } else {
                                    isCalling = false
                                    return getErrorModel(jsonObject, modelName)

                                }


                            } else {
                                return null
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }

                    }
                } else {
                    return null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        fun createImageFile(context: Context): File? {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "IMG_" + timeStamp + "_"
            val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(imageFileName, ".jpg", storageDir)
//            imageFilePath = image.absolutePath
            return image
        }

        fun addHour(myTime: String?, hour: Int, minute: Int): String? {
            try {
                val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val d = df.parse(myTime)
                val cal = Calendar.getInstance()
                cal.time = d
                cal.add(Calendar.HOUR, hour)
                cal.add(Calendar.MINUTE, minute)
                return df.format(cal.time)
            } catch (e: ParseException) {
                println(" Parsing Exception")
            }
            return null
        }

        private fun showDialogueForWarning(
                activity: Context,
                checkStateDataModel: JSONObject,
        ) {
            /*val builder = AlertDialog.Builder(activity).create()
            builder.setCancelable(false)
            val width = LinearLayout.LayoutParams.MATCH_PARENT
            val height = LinearLayout.LayoutParams.WRAP_CONTENT
            builder.window?.setLayout(width, height)

            val root =
                LayoutInflater.from(activity).inflate(R.layout.warning_force_layout, null)

            val btn_cancel = root.findViewById<BoldButton>(R.id.btn_cancel)
            val btn_update = root.findViewById<BoldButton>(R.id.btn_update)
            val tv_message = root.findViewById<TextView>(R.id.tv_message)
            val tv_title = root.findViewById<TextView>(R.id.tv_title)

            if (checkStateDataModel.has("state")) {

                if (checkStateDataModel.getString("state").equals("Force")) {
                    btn_cancel.visibility = View.GONE
                } else {
                    btn_cancel.visibility = View.VISIBLE
                }
            }

            if (checkStateDataModel.has("message")) {
                tv_message.text = checkStateDataModel.getString("message")
            }
            if (checkStateDataModel.has("title")) {
                tv_title.text = checkStateDataModel.getString("title")
            }
            // tv_title.text = checkStateDataModel?.title
            btn_cancel.setOnClickListener { builder.dismiss() }

            btn_update.setOnClickListener {


                val appPackageName =
                    activity.packageName // getPackageName() from Context or Activity object
                try {
                    val intent =
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                } catch (anfe: android.content.ActivityNotFoundException) {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    activity.startActivity(intent)
                }

            }
            builder.setView(root)


            builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            builder.show()
*/
        }

        fun showShortToast(str: String?, context: Context) {
            Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(str: String, context: Context) {
            Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show()
        }


        fun showLoader(activity: Context) {
            // loadingDialog = LoadingDialog.get(activity).show()


            try {
                if (dialogue != null) {
                    if (dialogue?.isShowing!!) {
                        dialogue?.dismiss()
                    }

                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            try {
                dialogue = Dialog(activity)
                //  dialogue?.setCancelable(false)
                dialogue?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogue?.setContentView(R.layout.common_loader)
                dialogue?.show()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }

        fun hideLoader() {
            try {
                if (dialogue != null && dialogue?.isShowing!!) {
                    dialogue?.dismiss()

                }

            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        fun capitalizeFirstLetter(str: String?): String {
            return if (str?.length == 0)
                str
            else if (str?.length == 1)
                str.toUpperCase()
            else
                str?.substring(0, 1)?.toUpperCase() + str?.substring(1)?.toLowerCase()
        }

        fun datefrom(date: String): String {
            var displayDate = ""
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
                val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
                val dateInString = formatterDisplay.parse(date)
                displayDate = formatter.format(dateInString)

            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return displayDate
        }

        fun date(date: String): String {
            var displayDate = ""
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
                val formatterDisplay = SimpleDateFormat("yyyy-MM-dd")
                val dateInString = formatterDisplay.parse(date)
                displayDate = formatter.format(dateInString)
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return displayDate
        }

        fun getCurrentDateTime(): String {
            var answer: String = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("hh:mm a, dd MMMM yyyy")
                answer = current.format(formatter)
                Log.d("answer", answer)
            } else {
                var date = Date()
                val formatter = SimpleDateFormat("hh:mm a, dd MMMM yyyy")
                answer = formatter.format(date)
                Log.d("answer", answer)
            }
            return answer

        }

        fun getCurrentDateTimeSimpleFormat(): String {
            var dateStr: String = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss")
                dateStr = current.format(formatter)
                Log.d("answer", dateStr)
            } else {
                var date = Date()
                val formatter = SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                dateStr = formatter.format(date)
                Log.d("answer", dateStr)
            }
            return dateStr
        }


        fun dateTo(date: String): String {
            var displayDate = ""
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'23:59:59.000'Z'")
                val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
                val dateInString = formatterDisplay.parse(date)
                displayDate = formatter.format(dateInString)
            } catch (e: ParseException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return displayDate

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
                if (isBackPressed) {
                    activity.finish()
                }


            }

            ivClose.setOnClickListener {
                builder?.dismiss()
            }
            builder.setView(root)

            builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            builder.show()
        }

        fun coverpicture(path: String?): Bitmap? {
            val mr = MediaMetadataRetriever()
            mr.setDataSource(path)
            val byte1 = mr.embeddedPicture
            mr.release()
            return if (byte1 != null) BitmapFactory.decodeByteArray(byte1, 0, byte1.size) else null
        }

        fun getfileExtension(context: Context, uri: Uri): String {
            val extension: String
            val contentResolver: ContentResolver = context.getContentResolver()
            val mimeTypeMap = MimeTypeMap.getSingleton()
            extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))!!
            return extension
        }

        fun expand(v: View) {
            v.measure(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            )
            val targtetHeight = v.measuredHeight
            v.layoutParams.height = 0
            v.visibility = View.VISIBLE
            val a: Animation = object : Animation() {
                override fun applyTransformation(
                        interpolatedTime: Float,
                        t: Transformation?,
                ) {
                    v.layoutParams.height =
                            if (interpolatedTime == 1f) RecyclerView.LayoutParams.WRAP_CONTENT else (targtetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
//        a.setDuration((targtetHeight / v.context.resources.displayMetrics.density).toInt().toLong())
            a.setDuration(300)
            v.startAnimation(a)
        }

        fun collapse(v: View) {
            val initialHeight = v.measuredHeight
            val a: Animation = object : Animation() {
                protected override fun applyTransformation(
                        interpolatedTime: Float,
                        t: Transformation?,
                ) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE


                    } else {
                        v.layoutParams.height =
                                initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
//        a.setDuration((initialHeight / v.context.resources.displayMetrics.density).toInt().toLong())
            a.setDuration(300)
            v.startAnimation(a)
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getFile(context: Context, uri: Uri?): File? {
            if (uri != null) {
                val path = getPath(context, uri)
                if (path != null && isLocal(path)) {
                    return File(path)
                }
            }
            return null
        }


        fun slideToBottom(view: View) {
            val animate = TranslateAnimation(0f, 0f, 0f, view.height.toFloat())
            animate.setDuration(500)
            animate.setFillAfter(true)
            view.startAnimation(animate)

            view.visibility = View.GONE
        }


        fun slideToTop(view: View, view1: View) {
            val animate = TranslateAnimation(0f, 0f, view.height.toFloat(), 0f)
            animate.setDuration(1000)
            animate.setFillAfter(true)
            view.startAnimation(animate)
            view.visibility = View.VISIBLE
            view1.visibility = View.VISIBLE
        }

        fun slideDown(view: View, view1: View?) {
            view.visibility = View.VISIBLE

            if (view1 != null) {
                view1.visibility = View.VISIBLE
            }
            val layoutParams = view.layoutParams
            layoutParams.height = 1
            view.layoutParams = layoutParams
            view.measure(
                    View.MeasureSpec.makeMeasureSpec(
                            Resources.getSystem().getDisplayMetrics().widthPixels,
                            View.MeasureSpec.EXACTLY
                    ),
                    View.MeasureSpec.makeMeasureSpec(
                            0,
                            View.MeasureSpec.UNSPECIFIED
                    )
            )
            val height = view.measuredHeight
            val valueAnimator: ValueAnimator = ObjectAnimator.ofInt(1, height)
            valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                override fun onAnimationUpdate(animation: ValueAnimator) {
                    val value = animation.getAnimatedValue() as Int
                    if (height > value) {
                        val layoutParams = view.layoutParams
                        layoutParams.height = value
                        view.layoutParams = layoutParams
                    } else {
                        val layoutParams = view.layoutParams
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        view.layoutParams = layoutParams
                    }
                }
            })
            valueAnimator.start()
        }


        fun slideUp(view: View) {
            view.post {
                val height = view.height
                val valueAnimator: ValueAnimator = ObjectAnimator.ofInt(height, 0)
                valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(animation: ValueAnimator) {
                        val value = animation.getAnimatedValue() as Int
                        if (value > 0) {
                            val layoutParams = view.layoutParams
                            layoutParams.height = value
                            view.layoutParams = layoutParams
                        } else {
                            view.visibility = View.GONE

                        }
                    }
                })
                valueAnimator.start()
            }
        }

        fun slideUp(view: View, view1: View) {
            view.post {
                val height = view.height
                val valueAnimator: ValueAnimator = ObjectAnimator.ofInt(height, 0)
                valueAnimator.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
                    override fun onAnimationUpdate(animation: ValueAnimator) {
                        val value = animation.getAnimatedValue() as Int
                        if (value > 0) {
                            val layoutParams = view.layoutParams
                            layoutParams.height = value
                            view.layoutParams = layoutParams
                        } else {
                            view.visibility = View.GONE
                            view1.visibility = View.GONE

                        }
                    }
                })
                valueAnimator.start()
            }
        }

        @Throws(IOException::class)
        fun createFile(context: Context): File {
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            return File.createTempFile(
                    "JPEG_${timeStamp}_", /* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
//                mCurrentPhotoPath = absolutePath
            }
        }


        fun checkCallPermission(context: Context): Boolean {
            val permission = Manifest.permission.CALL_PHONE
            val res: Int = checkSelfPermission(context, permission)
            return res == PackageManager.PERMISSION_GRANTED
        }


        fun isLocal(url: String?): Boolean {
            return url != null && !url.startsWith("http://") && !url.startsWith("https://")
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun getPath(context: Context, uri: Uri): String? {

            var contentUri: Uri? = null
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // LocalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                            docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    if ("primary".equals(type, ignoreCase = true)) {
                        return "" + Environment.getExternalStorageDirectory() + "/" + split[1]
                    }

                    // TODO handle non-primary volumes
                } else if (isDownloadsDocument(uri)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val id: String
                        var cursor: Cursor? = null
                        try {
                            cursor = context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null)
                            if (cursor != null && cursor.moveToFirst()) {
                                val fileName = cursor.getString(0)
                                val path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                                if (!TextUtils.isEmpty(path)) {
                                    return path
                                }
                            }
                        } finally {
                            cursor?.close()
                        }
                        id = DocumentsContract.getDocumentId(uri);
                        if (!TextUtils.isEmpty(id)) {
                            if (id.startsWith("raw:")) {
                                return id.replaceFirst("raw:", "")
                            }
                            val contentUriPrefixesToTry = arrayOf(
                                    "content://downloads/public_downloads",
                                    "content://downloads/my_downloads"
                            )
                            for (contentUriPrefix in contentUriPrefixesToTry) {
                                return try {
                                    val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), java.lang.Long.valueOf(id))
                                    getDataColumn(context, contentUri, null, null)
                                } catch (e: NumberFormatException) {
                                    //In Android 8 and Android P the id is not a number
                                    uri.path!!.replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "")
                                }
                            }
                        }
                    } else {
                        val id = DocumentsContract.getDocumentId(uri)
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:".toRegex(), "")
                        }
                        try {
                            contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                        } catch (e: java.lang.NumberFormatException) {
                            e.printStackTrace()
                        }
                        if (contentUri != null) {
                            return getDataColumn(context, contentUri, null, null)
                        }
                    }
                } /*else if (isDownloadsDocument(uri)) {

                    val id = DocumentsContract.getDocumentId(uri)
                    Log.e("TAGG", "getPath: " + id)
                    val contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            id?.toString()?.toLong()!!
                    )

                    return getDataColumn(context, contentUri, null, null)
                }*/ else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split =
                            docId.split((":").toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]

                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }

                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])

                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }// MediaProvider
                // DownloadsProvider
                // ExternalStorageProvider
            } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                        context,
                        uri,
                        null,
                        null
                )

            } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
                return uri.path
            }// File
            // MediaStore (and general)


            return null
        }


        fun isConnectedToInternet(context: Context): Boolean {
            val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                        } else {
                            TODO("VERSION.SDK_INT < M")
                        }
                    } else {
                        TODO("VERSION.SDK_INT < LOLLIPOP")
                    }
            if (capabilities != null) {
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        } else {
                            TODO("VERSION.SDK_INT < LOLLIPOP")
                        }
                ) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
            return false
        }

        fun getDataColumn(
                context: Context, uri: Uri?,
                selection: String?, selectionArgs: Array<String>?,
        ): String? {

            /*var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)

            try {
                cursor = context.contentResolver.query(
                        uri!!, projection,
                        selection, selectionArgs, null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null*/

            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                    column
            )

            try {
                cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs,
                        null)
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } catch (e: java.lang.IllegalArgumentException) {
                getFilePathFromURI(context, uri)
            } finally {
                cursor?.close()
            }
            return null
        }

        fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
            return Uri.parse(path.toString())
        }

        fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
            //create a file to write bitmap data
            var file: File? = null
            return try {
                file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
                file.createNewFile()

                //Convert bitmap to byte array
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
                val bitmapdata = bos.toByteArray()

                //write the bytes in file
                val fos = FileOutputStream(file)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()
                file
            } catch (e: Exception) {
                e.printStackTrace()
                file // it will return null
            }
        }

        /* private fun bitmapToFile(bitmap: Bitmap, context: Context): Uri {
             // Get the context wrapper
             val wrapper = ContextWrapper(context)

             // Initialize a new file instance to save bitmap object
             var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
             file = File(file, "${UUID.randomUUID()}.jpg")

             try {
                 // Compress the bitmap and save in jpg format
                 val stream: OutputStream = FileOutputStream(file)
                 Log.e("TAGG", "bitmapToFile: " + file.name + " " + file.path)
 //                bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
                 stream.flush()
                 stream.close()
             } catch (e: IOException) {
                 e.printStackTrace()
             }

             // Return the saved bitmap uri
             return Uri.parse(file.absolutePath)
         }*/

        fun getFilePathFromURI(context: Context?, contentUri: Uri?): String? {
            //copy file and send new file path
            val moviesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_MOVIES
            )
            var success = true
            if (!moviesDir.exists()) {
                success = moviesDir.mkdir()
            }
            if (success) {
                val fileName: String = getFileName(contentUri)!!
                if (!TextUtils.isEmpty(fileName)) {
                    val copyFile = File(moviesDir, "$fileName.mp4")
                    context?.let { copy(it, contentUri, copyFile) }
                    return copyFile.absolutePath
                }
            }
            return null
        }

        fun getFileName(uri: Uri?): String? {
            if (uri == null) return null
            var fileName: String? = null
            val path = uri.path
            val cut = path!!.lastIndexOf('/')
            if (cut != -1) {
                fileName = path.substring(cut + 1)
            }
            return fileName
        }

        fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
            try {
                val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
                val outputStream: OutputStream = FileOutputStream(dstFile)
                IOUtils.copyStream(inputStream, outputStream)
                inputStream.close()
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun saveBitmapToFile(file: File): File? {
            try {

                // BitmapFactory options to downsize the image
                val o = BitmapFactory.Options()
                o.inJustDecodeBounds = true
                o.inSampleSize = 6
                // factor of downsizing the image
                var inputStream = FileInputStream(file)
                //Bitmap selectedBitmap = null;
                BitmapFactory.decodeStream(inputStream, null, o)
                inputStream.close()

                // The new size we want to scale to
                val REQUIRED_SIZE = 75

                // Find the correct scale value. It should be the power of 2.
                var scale = 1
                while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                        o.outHeight / scale / 2 >= REQUIRED_SIZE
                ) {
                    scale *= 2
                }
                val o2 = BitmapFactory.Options()
                o2.inSampleSize = scale
                inputStream = FileInputStream(file)
                val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
                inputStream.close()

                // here i override the original image file
                file.createNewFile()
                val outputStream = FileOutputStream(file)
                selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                return file
            } catch (e: java.lang.Exception) {
                return null
            }
        }

        object CompressFile {
            fun getCompressedImageFile(file: File, mContext: Context?): File? {
                return try {
                    val o = BitmapFactory.Options()
                    o.inJustDecodeBounds = true
                    if (getFileExt(file.name) == "png" || getFileExt(file.name) == "PNG") {
                        o.inSampleSize = 6
                    } else {
                        o.inSampleSize = 6
                    }
                    var inputStream = FileInputStream(file)
                    BitmapFactory.decodeStream(inputStream, null, o)
                    inputStream.close()

                    // The new size we want to scale to
                    val REQUIRED_SIZE = 100

                    // Find the correct scale value. It should be the power of 2.
                    var scale = 1
                    while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                            o.outHeight / scale / 2 >= REQUIRED_SIZE
                    ) {
                        scale *= 2
                    }
                    val o2 = BitmapFactory.Options()
                    o2.inSampleSize = scale
                    inputStream = FileInputStream(file)
                    var selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
                    val ei = ExifInterface(file.absolutePath)
                    val orientation: Int = ei.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED
                    )
                    when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> selectedBitmap =
                                rotateImage(selectedBitmap, 90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> selectedBitmap =
                                rotateImage(selectedBitmap, 180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> selectedBitmap =
                                rotateImage(selectedBitmap, 270f)
                        ExifInterface.ORIENTATION_NORMAL -> {
                        }
                        else -> {
                        }
                    }
                    inputStream.close()


                    // here i override the original image file
                    val folder =
                            File(Environment.getExternalStorageDirectory().toString() + "/FolderName")
                    var success = true
                    if (!folder.exists()) {
                        success = folder.mkdir()
                    }
                    if (success) {
                        val newFile = File(File(folder.absolutePath), file.name)
                        if (newFile.exists()) {
                            newFile.delete()
                        }
                        val outputStream = FileOutputStream(newFile)
                        if (getFileExt(file.name) == "png" || getFileExt(file.name) == "PNG") {
                            selectedBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        } else {
                            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                        }
                        newFile
                    } else {
                        null
                    }
                } catch (e: java.lang.Exception) {
                    null
                }
            }

            fun getFileExt(fileName: String): String {
                return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
            }

            fun rotateImage(source: Bitmap?, angle: Float): Bitmap {
                val matrix = Matrix()
                matrix.postRotate(angle)
                return Bitmap.createBitmap(
                        source!!, 0, 0, source!!.width, source!!.height,
                        matrix, true
                )
            }
        }


    }


}