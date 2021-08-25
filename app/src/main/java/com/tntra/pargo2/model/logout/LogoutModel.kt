package com.tntra.pargo2.model.logout

data class LogoutModel(
    val message: String,
    val success: Boolean,
    val user: Any
)