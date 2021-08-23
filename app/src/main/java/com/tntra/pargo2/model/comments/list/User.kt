package com.tntra.pargo2.model.comments.list

data class User(
    val created_at: String,
    val email: String,
    val id: Int,
    val jti: String,
    val registration_tokens: List<Any>,
    val updated_at: String
)