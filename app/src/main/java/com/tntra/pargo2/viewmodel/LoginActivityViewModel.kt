package com.tntra.pargo2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo2.model.CommonResponseModel
import com.tntra.pargo2.model.login.OtpModel
import com.tntra.pargo2.model.login_response.UserLoginModel
import com.tntra.pargo2.model.logout.LogoutModel
import com.tntra.pargo2.repository.LoginRepository

class LoginActivityViewModel : ViewModel() {

    private var loginRepository: LoginRepository? = null
    var userModelData: MutableLiveData<UserModel>? = null

    //    var uploadImageModel: MutableLiveData<UploadImageModel>? = null
    var otpModel: MutableLiveData<OtpModel>? = null
    var userLoginModel: MutableLiveData<UserLoginModel>? = null
    var logout: MutableLiveData<LogoutModel>? = null
    var commonResponseModel: MutableLiveData<CommonResponseModel>? = null

    fun getLoginData(): LiveData<UserModel>? {
        return userModelData
    }

    fun getUserInfo(): LiveData<UserLoginModel>? {
        return userLoginModel
    }

    fun sendOtp(): LiveData<OtpModel>? {
        return otpModel
    }

    fun init(
            userId: String,
            password: String,
            grantType: String,
            authorizationToke: String,
            versionCode: Int,
            deviceName: String?,
            androidOS: String,
            deviceType: String?
    ) {
        loginRepository = LoginRepository().getInstance()
        userModelData = loginRepository!!.loginUser(
                userId,
                password,
                grantType,
                authorizationToke,
                versionCode,
                deviceName,
                androidOS
        )
    }

    fun initTwo(jsonObject: JsonObject) {
        loginRepository = LoginRepository().getInstance()
        userLoginModel = loginRepository!!.loginUserTwo(
                jsonObject
        )
    }

    fun callLogout(authorizationToke: String) {
        loginRepository = LoginRepository().getInstance()
        logout = loginRepository!!.logout(authorizationToke)
    }

    fun getLogout(): LiveData<LogoutModel>? {
        return logout
    }

    fun callApiStoreFCMToken(authorizationToke: String, jsonObject: JsonObject) {
        loginRepository = LoginRepository().getInstance()
        commonResponseModel = loginRepository!!.callApiStoreFCM(authorizationToke, jsonObject)
    }

    fun getStoreFCMToken(): LiveData<CommonResponseModel>? {
        return commonResponseModel
    }


//    fun uploadImage(
//        multiPart: MultipartBody.Part,
//        authorizationToke: String,
//        context: Context,
//        type: String
//    ) {
//        loginRepository = LoginRepository().getInstance()
//        uploadImageModel =
//            loginRepository?.uploadImage(multiPart, type, authorizationToke, context)
//    }
//
//    fun getImageUpload(): LiveData<UploadImageModel>? {
//        return uploadImageModel!!
//    }


}