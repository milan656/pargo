package com.tntra.pargo2.model.chatapi

data class Attributes(
    val body: String,
    val collab_room_id: Int,
    val formatted_created_at: String,
    val user_id: Int,
    val user_name: String,
    val user_profile_img_path: String
)