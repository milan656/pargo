package com.tntra.pargo.model.collabRoomList

data class Attributes(
    val collab_room_type: String,
    val generator_id: Int,
    val members: List<Member>,
    val message: String,
    val name: String
)