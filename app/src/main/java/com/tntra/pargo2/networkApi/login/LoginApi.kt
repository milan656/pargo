package com.walkins.aapkedoorstep.networkApi.login

import com.google.gson.JsonObject
import com.jkadvantagandbadsha.model.login.UserModel
import com.tntra.pargo2.model.CommonResponseModel
import com.tntra.pargo2.model.login_response.UserLoginModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {

    @POST("v1/auth/login")
    @FormUrlEncoded
    fun loginUser(
            @Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
            @Header("Authorization") authorizationToke: String
    ): Call<UserModel>

    @POST("v1/tyrepushpull/send-otp")
    fun callApiSendOTP(
            @Body jsonObject: JsonObject
    ): Call<ResponseBody>

    @POST("api/v1/register")
    fun callApiStoreFCM(
            @Header("Authorization") Authorization: String,
            @Body jsonObject: JsonObject
    ): Call<CommonResponseModel>

    @DELETE("api/v1/users/sign_out")
    fun logout(@Header("Authorization") Authorization: String): Call<CommonResponseModel>

    @POST("api/v1/users/sign_in")
    fun loginUserTwo(
            @Body jsonObject: JsonObject,

            /*@Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
            @Header("Authorization") authorizationToke: String,
            @Header("apk_version") versionCode: Int,
            @Header("mobile_model") deviceName: String?,
            @Header("mobile_os_version") androidOS: String*/
    ): Call<UserLoginModel>
    /* @POST("v1/tyrepushpull/send-otp")
     fun callApiSendOTP(
         @Body jsonObject: JsonObject
     ): Call<ResponseBody>

     @POST("v1/tyrepushpull/send-otp")
     fun loginUserTwo(
         @Body jsonObject: JsonObject,

         *//*@Field("username") userId: String, @Field("password") password: String, @Field("grant_type") grantType: String,
        @Header("Authorization") authorizationToke: String,
        @Header("apk_version") versionCode: Int,
        @Header("mobile_model") deviceName: String?,
        @Header("mobile_os_version") androidOS: String*//*
    ): Call<OtpModel>


    @POST("v1/auth/login")
    @FormUrlEncoded
    fun refreshToken(
        @Header("Authorization") authorizationToke: String,
        @Field("grant_type") grantType: String,
        @Field("refresh_token") refreshToken: String
    ): Call<UserModel>

    @Multipart
    @POST("v1/tyrepushpull/upload-image")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Header("Authorization") authorizationToke: String,
//        @Header("Content-Type") content:String,
        @Query("type") type: String
    ): Call<ResponseBody>*/


}