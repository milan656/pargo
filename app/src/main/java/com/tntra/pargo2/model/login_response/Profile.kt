package com.tntra.pargo2.model.login_response

data class Profile(
    val bio: String,
    val created_at: String,
    val experience: String,
    val id: Int,
    val location: String,
    val name: String,
    val occupation: String,
    val updated_at: String,
    val user_id: Int,
    val website: String
)