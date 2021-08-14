package com.tntra.pargo.common

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class PrefManager(context: Context) {
    private val PREFERENCE_NAME = "MyPref"
    private val activity: Context = context


    fun getPreferences(context: Context): SharedPreferences? {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun isLogin(UserRole: Boolean) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogin", UserRole)
        editor.commit()
    }

    fun isCreator(UserRole: Boolean) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isCreator", UserRole)
        editor.apply()
    }

    fun isCreatorType(UserRole: Boolean): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isCreator", false)
    }

    fun getIsLogin(): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLogin", false)
    }


    fun setEmailPreference(UserRole: Boolean?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("emailPreference", UserRole!!)
        editor.commit()
    }

    fun getEmailPreference(): Boolean? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("emailPreference", false)
    }


    fun setAccessToken(accessToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    fun setUserId(id: Int) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("id", id)
        editor.apply()
    }

    fun getUserId(): Int {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt("id", 0)
    }


    fun getAccessToken(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("accessToken", null)
    }


    fun setScreen(accessToken: ArrayList<String>) {


        val gson = Gson()
        val json = gson.toJson(accessToken)
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("screen", json)
        editor.commit()
    }

    fun getScreen(): ArrayList<String>? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        var arrayList = ArrayList<String>()
        val gson = Gson()
        val json = sharedPreferences.getString("screen", "")
        if (!json?.isEmpty()!!) {

            val type = object :
                    TypeToken<List<String?>?>() {}.type
            val arrPackageData =
                    gson.fromJson<List<String>>(json, type)
            for (data in arrPackageData) {
                arrayList?.add(data)
            }
        }



        return arrayList
    }

    fun setMenu(accessToken: ArrayList<String>) {


        val gson = Gson()
        val json = gson.toJson(accessToken)
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("Menu", json)
        editor.commit()
    }

    fun getMenu(): ArrayList<String>? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        var arrayList = ArrayList<String>()
        val gson = Gson()
        val json = sharedPreferences.getString("Menu", "")
        if (!json?.isEmpty()!!) {

            val type = object :
                    TypeToken<List<String?>?>() {}.type
            val arrPackageData =
                    gson.fromJson<List<String>>(json, type)
            for (data in arrPackageData) {
                arrayList?.add(data)
            }
        }



        return arrayList
    }


    fun setOwnerName(accessToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("ownerName", accessToken)
        editor.commit()
    }

    fun setBetteryPopUp(b: Boolean) {

        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("betterypopup", b)
        editor.commit()

    }


    fun getSavePreference(): Boolean? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_service_preference_set", false)
    }

    fun setSavePreference(b: Boolean?) {

        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_service_preference_set", b!!)
        editor.commit()

    }

    fun getAutoStartOn(): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("autostart", false)
    }

    fun setAutoStartOn(b: Boolean) {

        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("autostart", b)
        editor.commit()

    }

    fun getBetteryPopUp(): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("betterypopup", false)
    }


    fun getOwnerName(): String {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("ownerName", null)!!
    }

    fun setBussinessName(accessToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("businessName", accessToken)
        editor.commit()
    }


    fun getBussinessName(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("businessName", null)
    }


    fun setAccessTokenExpireDate(accessToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("accessTokenExpireDate", accessToken)
        editor.commit()
    }


    fun getAccessTokenExpireDate(): String {
        var expireToken = ""


        try {
            val sharedPreferences =
                    activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            expireToken = sharedPreferences.getString("accessTokenExpireDate", null)!!
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return expireToken


    }

    fun setType(accessToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("type", accessToken)
        editor.commit()
    }


    fun getType(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("type", null)
    }


    fun setPermissionList(accessToken: ArrayList<String>) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()


        val colorsSet: MutableSet<String> = HashSet()

        // add all the colors to the Set

        // add all the colors to the Set
        colorsSet.addAll(accessToken)

        editor.putStringSet("permission", colorsSet)
        editor.commit()
    }


    fun getPermission(): ArrayList<String> {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        val colorsSet: MutableSet<String>? = sharedPreferences.getStringSet(
                "permission",
                null
        )

        val arrayList = ArrayList<String>()
        if (colorsSet != null && colorsSet.size > 0) {
            for (item in colorsSet) {
                arrayList.add(item)
            }
        }
        return arrayList
    }

    fun setDealerType(accessToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("type_dealer", accessToken)
        editor.commit()
    }


    fun getDealerType(): String {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("type_dealer", null)!!
    }

    fun setRefreshToken(refreshToken: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("refreshToken", refreshToken)
        editor.commit()
    }

    fun setNumber(number: String) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("number", number)
        editor.commit()
    }

    fun getNumber(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("number", null)
    }

    fun getRefreshToken(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("refreshToken", null)
    }

    fun setToken(token: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.commit()
    }


    fun getToken(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("token", null)
    }

    fun setUuid(uuid: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uuid", uuid)
        editor.commit()
    }


    fun getUuid(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("uuid", null)
    }

    fun setSapId(uuid: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("sapId", uuid)
        editor.commit()
    }


    fun getSapId(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("sapId", null)
    }

    fun setImageUrl(imageUrl: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imageUrl", imageUrl)
        editor.commit()

    }

    fun getImageUrl(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("imageUrl", "")
    }

    fun setUserName(userName: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userName", userName)
        editor.commit()
    }

    fun getuserName(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("userName", "")
    }

    fun setValue(key: String, value: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.commit()
    }


    fun remove(key: String) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
    }


    fun removeValue(key: String) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.commit()
    }

    fun getValue(key: String): String {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "")!!
    }

    fun saveArrayList(key: String, vehicleList: ArrayList<String>?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val textList: MutableList<String> = ArrayList()
        textList.addAll(vehicleList!!)
        val jsonText = gson.toJson(textList)
        try {
            editor.putString(key, jsonText)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        editor.commit()
    }


    fun saveArray(key: String, list: ArrayList<String>) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
//        val textList: List<String> = ArrayList(data)
        val jsonText = gson.toJson(list)
        editor.putString(key, jsonText)
        editor.apply()
    }

    fun setGoogleRatingLink(googleRating: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("googleRating", googleRating)
        editor.commit()
    }

    fun getGoogleRating(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("googleRating", "")
    }


    fun setVideoUrl(videoURL: String?) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("videoURL", videoURL)
        editor.commit()

    }

    fun getVideoUrl(): String? {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString("videoURL", "")
    }

    fun isWelcome(UserRole: Boolean) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isWelcome", UserRole)
        editor.commit()
    }

    fun getIsWelcome(): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isWelcome", false)
    }

    fun isOnBoading(UserRole: Boolean) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isOnBoading", UserRole)
        editor.commit()
    }

    fun getIsOnBoading(): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isOnBoading", false)
    }

    fun isProfile(UserRole: Boolean) {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isProfile", UserRole)
        editor.commit()
    }

    fun getIsProfile(): Boolean {
        val sharedPreferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isProfile", false)
    }

    fun clearAll() {

        val preferences = activity.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.clear()
        editor.commit()

    }


}