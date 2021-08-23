package com.tntra.pargo2.model.collabRoomList

data class CollabRoomListModel(
    val collab_rooms: List<CollabRoom>,
    val message: String,
    val success: Boolean
)