package com.tntra.pargo.model.content_list.show

data class Attributes(
    val body: String,
    val posts_path: List<String>,
    val title: String,
    val total_comments: Int,
    val total_likes: Int,
    val total_visits: Int
)