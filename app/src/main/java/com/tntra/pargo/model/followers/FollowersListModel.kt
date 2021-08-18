package com.tntra.pargo.model.followers

data class FollowersListModel(
    val follows: List<Follow>,
    val message: String,
    val page: String,
    val records: Int,
    val success: Boolean,
    val type: String
)