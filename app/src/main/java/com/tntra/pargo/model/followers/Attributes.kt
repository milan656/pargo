package com.tntra.pargo.model.followers

data class Attributes(
    val followers: List<Follower>,
    val followers_count: Int,
    val followings: List<Any>,
    val followings_count: Int
)