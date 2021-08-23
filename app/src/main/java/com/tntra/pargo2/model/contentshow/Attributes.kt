package com.tntra.pargo2.model.contentshow

data class Attributes(
    val accessibility: String,
    val body: String,
    val cover_img_path: String,
    val created_at: String,
    val duration: String,
    val genre: Genre,
    val name: String,
    val post_type: Any,
    val posts_path: String,
    val title: String,
    val total_comments: Int,
    val total_likes: Int,
    val total_visits: Int,
    val user_id: Int,
    val user_profile_img_path: String
)