package com.jkadvantagandbadsha.model.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDetailModel(

        @SerializedName("id")
        @Expose var id: String,
        @SerializedName("sap_id")
        @Expose var sap_id: String,
        @SerializedName("uuid")
        @Expose var uuid: String,
        @SerializedName("type")
        @Expose var type: String,
        @SerializedName("dealer_type")
        @Expose var dealer_type: String?,
        @SerializedName("area_name")
        @Expose var area_name: String,
        @SerializedName("owner_name")
        @Expose var owner_name: String,
        @SerializedName("customer_class")
        @Expose var customer_class: String,
        @SerializedName("is_login_first_time")
        @Expose var is_login_first_time: Boolean,
        @SerializedName("permissions")
        @Expose var arrayListPermission: ArrayList<String>

)