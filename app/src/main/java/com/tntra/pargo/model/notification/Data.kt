package com.tntra.pargo.model.notification

data class Data(
    val collab_room_id: Int,
    val member_id: Int,
    val redirectTo: String,
    val type: String
)