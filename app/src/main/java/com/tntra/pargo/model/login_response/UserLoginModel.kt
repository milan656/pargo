package com.tntra.pargo.model.login_response

data class UserLoginModel(
    val message: String,
    val success: Boolean,
    var authorization:String,
    val user: User
)