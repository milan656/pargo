package com.tntra.pargo2.model.chatapi

data class ChatListApiModel(
    val message: String,
    val messages: List<Message>,
    val success: Boolean
)