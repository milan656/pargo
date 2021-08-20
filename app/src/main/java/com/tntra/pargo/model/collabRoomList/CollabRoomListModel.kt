package com.tntra.pargo.model.collabRoomList

data class CollabRoomListModel(
    val collab_rooms: List<CollabRoom>,
    val message: String,
    val success: Boolean
)