package com.tntra.pargo2.model.logout

data class LogoutModel(
    var message: String,
    var success: Boolean,
    var user: Any?
)