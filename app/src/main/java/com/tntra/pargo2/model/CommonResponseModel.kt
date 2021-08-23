package com.tntra.pargo2.model

data class CommonResponseModel(
    val message: String,
    val errors: String,
    val success: Boolean
)