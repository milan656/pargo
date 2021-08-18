package com.tntra.pargo.model.collabroom

data class CollabRoomCreateModel(
    val collab_room: CollabRoom,
    val message: String,
    val success: Boolean
)