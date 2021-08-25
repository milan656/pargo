package com.tntra.pargo2.model.chat_users

data class SendChatData(
        var from: String,
        var to: String,
        var content: String
)