package com.tntra.pargo.model.collabroom

data class CollabRoomModel(
    val collab_rooms: List<CollabRoom>,
    val message: String,
    val success: Boolean
)