package com.tntra.pargo.model.followers

data class FollowerListModel(
    val follows: List<Follow>,
    val message: String,
    val success: Boolean

)