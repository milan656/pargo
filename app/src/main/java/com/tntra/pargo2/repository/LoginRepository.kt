package com.tntra.pargo2.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo2.common.Common
import com.tntra.pargo2.common.RetrofitCommonClass
import com.tntra.pargo2.model.CommonResponseModel
import com.tntra.pargo2.model.content_list.ContentListModel
import com.tntra.pargo2.model.login.OtpModel
import com.tntra.pargo2.model.login_response.UserLoginModel
import com.tntra.pargo2.model.logout.LogoutModel
import com.walkins.aapkedoorstep.networkApi.login.LoginApi
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Header
import java.io.IOException


class LoginRepository {

    var loginDataRespotitory: LoginRepository? = null

    var loginApi: LoginApi

    constructor() {
        loginApi = RetrofitCommonClass.createService(LoginApi::class.java)
    }


    companion object

    fun getInstance(): LoginRepository {
        if (loginDataRespotitory == null) {
            loginDataRespotitory = LoginRepository()
        }
        return loginDataRespotitory as LoginRepository
    }


    fun loginUser(
            userId: String,
            password: String,
            grantType: String,
            authorizationToke: String,
            versionCode: Int,
            deviceName: String?,
            androidOS: String
    ): MutableLiveData<UserModel> {
        val loginData = MutableLiveData<UserModel>()
        loginApi.loginUser(
                userId,
                password,
                grantType,
                authorizationToke
        ).enqueue(object : Callback<UserModel> {
            override fun onResponse(
                    call: Call<UserModel>,
                    response: Response<UserModel>
            ) {
                if (response.isSuccessful) {
                    loginData.value = response.body()
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)

                        val userModel: UserModel =
                                Common.getErrorModel(jsonObjectError, "UserModel") as UserModel
                        //recoveryData.setValue(recoveryPasswordModel)


                        loginData.value = userModel

                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {

                t.printStackTrace()
            }
        })
        return loginData
    }

    fun callApiSendOtp(
            jsonObject: JsonObject,
            context: Context
    ): MutableLiveData<OtpModel> {
        var servicedata = MutableLiveData<OtpModel>()

        var addEdit: Call<ResponseBody>? = loginApi.callApiSendOTP(jsonObject)

        addEdit?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
            ) = if (response.isSuccessful) {
                servicedata.value = Common?.getModelreturn(
                        "OtpModel",
                        response,
                        0,
                        context
                ) as OtpModel?

            } else {
                try {
                    servicedata.value = Common?.getModelreturn(
                            "OtpModel",
                            response,
                            1,
                            context
                    ) as OtpModel?
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("getCipResponse", "" + t.cause + " " + t.message)
//                servicedata.value = null
            }
        })
        return servicedata
    }

    fun callApiStoreFCM(
            authorizationToke: String,
            jsonObject: JsonObject,

            ): MutableLiveData<CommonResponseModel> {
        val servicedata = MutableLiveData<CommonResponseModel>()

        val addEdit: Call<CommonResponseModel> = loginApi.callApiStoreFCM(authorizationToke, jsonObject)

        addEdit.enqueue(object : Callback<CommonResponseModel> {
            override fun onResponse(
                    call: Call<CommonResponseModel>, response: Response<CommonResponseModel>
            ) = if (response.isSuccessful) {
                servicedata.value = response.body()

            } else {
                try {
                    val responce = response.errorBody()?.string()
                    val jsonObjectError = JSONObject(responce)

                    val CommonResponseModel: CommonResponseModel =
                            Common.getErrorModel(jsonObjectError, "CommonResponseModel") as CommonResponseModel
                    //recoveryData.setValue(recoveryPasswordModel)

                    servicedata.value = CommonResponseModel

                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onFailure(call: Call<CommonResponseModel>, t: Throwable) {
                Log.e("getCipResponse", "" + t.cause + " " + t.message)
//                servicedata.value = null
            }
        })
        return servicedata
    }

    fun loginUserTwo(
            jsonObject: JsonObject/*,

        userId: String,
        password: String,
        grantType: String,
        authorizationToke: String,
        versionCode: Int,
        deviceName: String?,
        androidOS: String*/
    ): MutableLiveData<UserLoginModel> {
        val loginData = MutableLiveData<UserLoginModel>()
        loginApi.loginUserTwo(
                jsonObject,

                /* userId,
             password,
             grantType,
             authorizationToke,
             versionCode,
             deviceName,
             androidOS*/
        ).enqueue(object : Callback<UserLoginModel> {
            override fun onResponse(
                    call: Call<UserLoginModel>,
                    response: Response<UserLoginModel>
            ) {
                if (response.isSuccessful) {
                    response.body().authorization = response.headers().get("authorization")!!
                    Log.e("TAGG", "onResponse: " + response.body().authorization)
                    val model = response.body()
                    loginData.value = model
                } else {
                    try {
                        val responce = response.errorBody()?.string()

                        val jsonObjectError = JSONObject(responce)

                        val userModel: UserLoginModel =
                                Common.getErrorModel(jsonObjectError, "UserLoginModel") as UserLoginModel
                        loginData.value = userModel
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<UserLoginModel>, t: Throwable) {

            }
        })
        return loginData
    }

    fun logout(authorizationToke: String
    ): MutableLiveData<ResponseBody> {
        val loginData = MutableLiveData<ResponseBody>()
        loginApi.logout(
                authorizationToke,
        ).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.e("TAGlogout", "onResponse: " + response)

                    val resp = response.body()?.string()
                    Log.e("TAGlogout", "onResponse: " + resp)
                    Log.e("TAGlogout", "onResponse: " + response.raw().body())

//                    loginData.value = model
                } else {
                    try {
                        val responce = response.errorBody()?.string()
                        val jsonObjectError = JSONObject(responce)
                        val userModel: LogoutModel =
                                Common.getErrorModel(jsonObjectError, "LogoutModel") as LogoutModel
//                        loginData.value = userModel
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

            }
        })
        return loginData
    }


}