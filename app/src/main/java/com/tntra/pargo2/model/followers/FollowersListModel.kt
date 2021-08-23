package com.tntra.pargo2.model.followers

data class FollowersListModel(
    val follows: List<Follow>,
    val message: String,
    val page: String,
    val records: Int,
    val success: Boolean,
    val type: String
)