package com.tntra.pargo.model.followers

data class Follower(
    val created_at: String,
    val email: String,
    val id: Int,
    val jti: String,
    val registration_tokens: List<Any>,
    val updated_at: String
)