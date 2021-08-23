package com.tntra.pargo2.common

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tntra.pargo2.R
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nir21 on 23-01-2018.
 */
fun AppCompatActivity.replaceFragmenty(
    fragment: Fragment,
    allowStateLoss: Boolean = false,
    @IdRes containerViewId: Int
) {
    val ft = supportFragmentManager
        .beginTransaction()
        .replace(containerViewId, fragment)
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.showShortToast(str: String, context: Context) {
    Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showLongToast(str: String, context: Context) {
    Toast.makeText(context, "" + str, Toast.LENGTH_LONG).show()
}

fun AppCompatActivity.showDialogue(activity: Activity, message: String) {
    val builder = AlertDialog.Builder(activity).create()
    builder.setCancelable(false)
    val root = LayoutInflater.from(activity).inflate(R.layout.common_dialogue_layout, null)

    builder.setView(root)


    builder.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    builder.show()


}


fun AppCompatActivity.isValidEmail(target: String): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}


fun AppCompatActivity.dateFotmat(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString = formatter.parse(date)

    val displayDate:String = try {
        formatterDisplay.format(dateInString)
    } catch (e: Exception) {
        ""
    }
    return displayDate

}


fun AppCompatActivity.dateFotmatWithTime(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val formatterDisplay = SimpleDateFormat("dd-MM-yyyy HH:mm a")
    val dateInString = formatter.parse(date)
    val displayDate:String = try {
        formatterDisplay.format(dateInString)
    } catch (e: Exception) {
        ""
    }
    return displayDate

}



fun AppCompatActivity.getFormatedValue(date: Int): String {
    val formatter = DecimalFormat("#,###,###")
    val returnValus = formatter.format(date)

    return returnValus
}


fun AppCompatActivity.dateFotmatInDate(date: String): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    val returnDate = formatter.parse(date)
    return returnDate

}


fun AppCompatActivity.dateDifference(dateInPref: String): Long {

    val dateAccessToken = dateFotmatInDate(dateInPref)
    val date = Date()

    val diff = dateAccessToken.time - date.time
    val seconds = diff / 1000

    val minutes = seconds / 60
    val hours = minutes / 60

    Log.i("hours","++++"+hours)
    val days = hours / 24
    return days
}

fun AppCompatActivity.dateForWebservice(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString = formatterDisplay.parse(date)
    val displayDate = formatter.format(dateInString)
    return displayDate

}
fun AppCompatActivity.dateForWebservice_2(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val formatterDisplay = SimpleDateFormat("yyyy-MM-dd")
    val dateInString = formatterDisplay.parse(date)
    val displayDate = formatter.format(dateInString)
    return displayDate

}
fun AppCompatActivity.dateTFormatTo(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val formatterDisplay = SimpleDateFormat("HH:mm a, dd MMMM yyyy")
    val dateInString = formatterDisplay.parse(date)
    val displayDate = formatter.format(dateInString)
    return displayDate

}
fun AppCompatActivity.dateTFormatTo_(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
    val formatterDisplay = SimpleDateFormat("HH:mm a, dd MMMM yyyy")
    val dateInString = formatterDisplay.parse(date)
    val displayDate = formatter.format(dateInString)
    return displayDate

}

fun AppCompatActivity.datefrom(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'00:00:00.000'Z'")
    val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString = formatterDisplay.parse(date)
    val displayDate = formatter.format(dateInString)
    return displayDate

}

fun AppCompatActivity.dateTo(date: String): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'23:59:59.000'Z'")
    val formatterDisplay = SimpleDateFormat("dd-MM-yyyy")
    val dateInString = formatterDisplay.parse(date)
    val displayDate = formatter.format(dateInString)
    return displayDate

}

fun AppCompatActivity.checkPermission(context: Context): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                    124
                )
            }
            return false
        } else {
            return true
        }
    } else {
        return true
    }

}

fun Fragment.checkPermission(context: FragmentActivity?): Boolean {
    val currentAPIVersion = Build.VERSION.SDK_INT
    if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } !== PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                ||  ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.CAMERA
                )) {
                val alertBuilder = AlertDialog.Builder(context)
                alertBuilder.setCancelable(true)
                alertBuilder.setTitle("Permission necessary")
                alertBuilder.setMessage("External storage permission is necessary")
                alertBuilder.setPositiveButton(
                    android.R.string.yes
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.CAMERA),
                        123
                    )
                }
                val alert = alertBuilder.create()
                alert.show()
            } else {
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE ,  Manifest.permission.CAMERA),
                    123
                )

            }
            return false
        } else {
            return true
        }
    } else {
        return true
    }

}




fun AppCompatActivity.getOutputMediaFile(): File? {
    val mediaStorageDir = File(
        Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        ), "CameraDemo"
    )

    if (!mediaStorageDir.exists()) {
        if (!mediaStorageDir.mkdirs()) {
            return null
        }
    }

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    return File(
        mediaStorageDir.path + File.separator +
                "IMG_" + timeStamp + ".jpg"
    )

}

@RequiresApi(Build.VERSION_CODES.KITKAT)
fun AppCompatActivity.getPath(context: Context, uri: Uri): String? {
    val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            if ("primary".equals(type, ignoreCase = true)) {
                return (Environment.getExternalStorageDirectory().toString() + "/"
                        + split[1])
            }
        } else if (isDownloadsDocument(uri)) {

            val id = DocumentsContract.getDocumentId(uri)
            val contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/public_downloads"),
                java.lang.Long.valueOf(id)
            )

            return getDataColumn(context, contentUri, null, null)
        } else if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val type = split[0]

            var contentUri: Uri? = null
            if ("image" == type) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "_id=?"
            val selectionArgs = arrayOf(split[1])

            return getDataColumn(
                context, contentUri, selection,
                selectionArgs
            )
        }// MediaProvider
        // DownloadsProvider
    } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {

        // Return the remote address
        return if (isGooglePhotosUri(uri) || isGoogleDriveUri(uri)) uri.lastPathSegment else getDataColumn(
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


fun getDataColumn(
    context: Context, uri: Uri?,
    selection: String?, selectionArgs: Array<String>?
): String? {

    var cursor: Cursor? = null
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
    return null
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is ExternalStorageProvider.
 */
fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri
        .authority
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is DownloadsProvider.
 */
fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri
        .authority
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is MediaProvider.
 */
fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri
        .authority
}

/**
 * @param uri
 * The Uri to check.
 * @return Whether the Uri authority is Google Photos.
 */
fun isGooglePhotosUri(uri: Uri): Boolean {
    return "com.google.android.apps.photos.content" == uri
        .authority
}

/**
 * @param uri The Uri to check.
 * @return Whether the Uri authority is Google Drive.
 */
fun isGoogleDriveUri(uri: Uri): Boolean {
    return "com.google.android.apps.docs.storage" == uri.authority
}

