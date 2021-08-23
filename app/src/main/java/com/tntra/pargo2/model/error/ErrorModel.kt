package com.jkadvantage.model.error


import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("status")
    var status: Int,
    @SerializedName("statusCode")
    var statusCode: Int
)