package com.tntra.pargo2.model.collabroom

data class CollabRoomCreateModel(
    val collab_room: CollabRoom,
    val message: String,
    val success: Boolean
)