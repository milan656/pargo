package com.tntra.pargo.model.followers

data class FollowerModel(
    val follows: Follows?,
    val message: String,
    val success: Boolean,
    var isChecked: Boolean,
    var name: String,
    var passion: String
)