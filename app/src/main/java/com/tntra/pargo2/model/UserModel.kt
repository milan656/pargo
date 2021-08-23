package com.jkadvantagandbadsha.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jkadvantage.model.error.ErrorModel

data class UserModel(

    @SerializedName("success")
    @Expose var success: Boolean = true,
    @SerializedName("accessTokenExpiresAt")
    @Expose var accessTokenExpiresAt: String,
    @SerializedName("accessToken")
    @Expose var accessToken: String,
    @SerializedName("refreshToken")
    @Expose var refreshToken: String,
    @SerializedName("refreshTokenExpiresAt")
    @Expose var refreshTokenExpiresAt: String,
    @SerializedName("token")
    @Expose var token: String,
    @SerializedName("message")
    @Expose var message: String,
    @SerializedName("name")
    @Expose var name: String,
    @SerializedName("videoURL")
    @Expose var videoURL: String?,
    @SerializedName("user")
    @Expose var userDetailModel: UserDetailModel?,
    @SerializedName("dealer_type")
    @Expose var dealerType: String?,
    @SerializedName("customer_class_type")
    @Expose var customer_class_type: String?,
    @SerializedName("display_type")
    @Expose var display_type: String?,
    @SerializedName("error")
    @Expose
    val error: ArrayList<ErrorModel>

)