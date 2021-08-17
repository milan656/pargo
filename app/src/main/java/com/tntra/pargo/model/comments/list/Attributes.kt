package com.tntra.pargo.model.comments.list

data class Attributes(
    val content_id: Int,
    val message: String,
    val user_profile_img_path: String,
    val user: User
)