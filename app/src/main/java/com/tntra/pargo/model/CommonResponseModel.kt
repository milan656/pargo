package com.tntra.pargo.model

data class CommonResponseModel(
    val message: String,
    val errors: String,
    val success: Boolean
)