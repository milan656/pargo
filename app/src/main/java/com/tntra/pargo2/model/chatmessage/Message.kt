package com.tntra.pargo2.model.chatmessage

data class Message(val userName: String, val messageContent: String, val roomName: String, var viewType: Int,
val profileImage:String,
val time:String)